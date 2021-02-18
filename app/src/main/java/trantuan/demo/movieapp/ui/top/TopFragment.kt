@file:Suppress("DEPRECATION")

package trantuan.demo.movieapp.ui.top

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.model.movieTop.ResultTop
import trantuan.demo.movieapp.databinding.FragmentTopBinding

class TopFragment : Fragment() {

    private var currentPage = 1
    private var totalPage = 1
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount: Int = 0

    private lateinit var binding: FragmentTopBinding
    private lateinit var viewModel: TopViewModel
    private lateinit var topAdapter: TopMovieAdapter
    private var listTop: ArrayList<ResultTop?> = ArrayList()

    var isLoading = false
    private var isRefresh = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container, false)
        viewModel = ViewModelProviders.of(this).get(TopViewModel::class.java)

        binding.topViewModel = viewModel

        topAdapter = TopMovieAdapter(context, listTop)

        binding.rvListTop.layoutManager = GridLayoutManager(context, 2)

        binding.rvListTop.adapter = topAdapter

        viewModel.loadTop(currentPage)

        initScrollListener()

        subscriber()

        refresh()

        return binding.root
    }

    private fun initScrollListener() {
        binding.rvListTop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                if (!isLoading&& !isRefresh) {
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
        listTop.add(null)
        topAdapter.notifyItemInserted(listTop.size - 1)
        viewModel.loadTop(currentPage)
    }

    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            isRefresh = true
            listTop.clear()
            currentPage = 1
            this@TopFragment.isLoading = false
            viewModel.loadTop(currentPage)                  // refresh your list contents somehow
            swipeRefresh.isRefreshing = false               // reset the SwipeRefreshLayout (stop the loading spinner)
        }
    }

    private fun subscriber() = with(binding.topViewModel!!) {
        result.observe(viewLifecycleOwner, Observer {
            totalPage = it.totalPages
            if (isRefresh){
                listTop.clear()
                isRefresh = false
            }
            if (this@TopFragment.isLoading) {
                listTop.removeAt(listTop.size - 1)
                val scrollPosition: Int = listTop.size
                topAdapter.notifyItemRemoved(scrollPosition)
                this@TopFragment.isLoading = false
            }
            listTop.addAll(it.results)
            topAdapter.notifyDataSetChanged()
        })
    }
}