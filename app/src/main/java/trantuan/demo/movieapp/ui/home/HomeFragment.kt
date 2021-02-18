@file:Suppress("DEPRECATION")

package trantuan.demo.movieapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_item.view.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.model.movieHome.Movies
import trantuan.demo.movieapp.databinding.FragmentHomeBinding
import trantuan.demo.movieapp.ui.detail.DetailActivity
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var currentPage = 1
    private var totalPage = 1
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount: Int = 0

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private var displayList: ArrayList<Movies?> = ArrayList()
    private lateinit var searchView: EditText


    var isLoading = false
    private var isRefresh = false
    private var isSearch = false

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        binding.homeModel = viewModel

        homeAdapter = HomeAdapter(context, displayList) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)

            Log.e("int", "onCreateView: ${it.id}")
        }
        binding.rvMovieList.layoutManager = GridLayoutManager(context, 3)
        binding.rvMovieList.adapter = homeAdapter
        viewModel.loadData(currentPage)

        searchView = binding.search.edt_search_text

        subscriber()

        refresh()

        search()

        initScrollListener()

        return binding.root
    }

    private fun initScrollListener() {
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                @NonNull recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                totalItemCount = linearLayoutManager!!.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && !isRefresh) {
                    if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        //bottom of list!
                        if (currentPage < totalPage) {
                            currentPage++
                            loadMore()
                            isLoading = true
                        }
                    }
                }
            }
        })
    }

    private fun loadMore() {
        displayList.add(null)
        homeAdapter.notifyItemInserted(displayList.size - 1)
        viewModel.loadData(currentPage)
    }

    private fun search() {
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    homeAdapter.updateListItem(displayList)
                } else {
                    filterList(s.toString())
                }
            }

        })
    }

    private fun filterList(filterItem: String) {
        val tempList: ArrayList<Movies?> = ArrayList()

        if (filterItem.isEmpty()) {
            displayList.let {
                tempList.addAll(it)
            }
        } else {
            displayList.forEach {
                if (filterItem in it!!.title.toLowerCase(Locale.ROOT)) {
                    tempList.add(it)
                }
            }
        }
//        for (d in displayList) {
//            d?.let {
//                if (filterItem in it.title.toLowerCase(Locale.ROOT)) {
//                    tempList.add(d)
//                }
//            }
//        }
        homeAdapter.updateListItem(tempList)
    }

    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            isRefresh = true
            displayList.clear()
            currentPage = 1
            this@HomeFragment.isLoading = false
            viewModel.loadData(currentPage)             // refresh your list contents somehow
            swipeRefresh.isRefreshing = false           // reset the SwipeRefreshLayout (stop the loading spinner)
        }
    }

    private fun subscriber() = with(binding.homeModel!!) {
        result.observe(viewLifecycleOwner, Observer {
            totalPage = it.totalPages
            if (isRefresh) {
                displayList.clear()
                isRefresh = false
            }

            if (this@HomeFragment.isLoading) {
                displayList.removeAt(displayList.size - 1)
                val scrollPosition: Int = displayList.size
                homeAdapter.notifyItemRemoved(scrollPosition)
                this@HomeFragment.isLoading = false
            }
            displayList.addAll(it.results)
            homeAdapter.notifyDataSetChanged()
        })
    }
}
