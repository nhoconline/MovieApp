@file:Suppress("DEPRECATION")

package trantuan.demo.movieapp.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.model.movieUpcoming.ResultUpcoming
import trantuan.demo.movieapp.databinding.FragmentUpcomingBinding
import trantuan.demo.movieapp.ui.detail.DetailActivity

class UpcomingFragment : Fragment() {

    private var currentPage = 1
    private var totalPage = 1
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount: Int = 0
    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var viewModel: UpViewModel
    private lateinit var upAdapter: UpcomingAdapter
    private var listUp: ArrayList<ResultUpcoming?> = ArrayList()
    private var isRefresh = false

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming, container, false)
        viewModel = ViewModelProviders.of(this).get(UpViewModel::class.java)
        binding.upViewModel = viewModel
        binding.rvUpcoming.layoutManager = GridLayoutManager(context, 2)
        upAdapter = UpcomingAdapter(context, listUp)
        binding.rvUpcoming.adapter = upAdapter
        viewModel.loadUp(currentPage)

        initScrollListener()

        subscriber()

        refresh()

        return binding.root
    }


    private fun initScrollListener() {
        binding.rvUpcoming.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                @NonNull recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                totalItemCount = linearLayoutManager!!.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && !isRefresh) {
                    Log.d("kevin", "onScrolled() returned: ${lastVisibleItem + visibleThreshold}")
                    if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        //bottom of list!
                        Log.e("kevin", "on load more: ")
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
        listUp.add(null)
        upAdapter.notifyItemInserted(listUp.size - 1)
        viewModel.loadUp(currentPage)
    }


    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            isRefresh = true
            listUp.clear()
            currentPage = 1
            this@UpcomingFragment.isLoading = false
            viewModel.loadUp(currentPage)       // refresh your list contents somehow
            swipeRefresh.isRefreshing =
                false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }
    }

    private fun subscriber() = with(binding.upViewModel!!) {
        result.observe(viewLifecycleOwner, Observer {
            totalPage = it.totalPages
            if (isRefresh) {
                listUp.clear()
                isRefresh = false
            }
            if (this@UpcomingFragment.isLoading) {
                listUp.removeAt(listUp.size - 1)
                val scrollPosition: Int = listUp.size
                upAdapter.notifyItemRemoved(scrollPosition)
                this@UpcomingFragment.isLoading = false
            }
            listUp.addAll(it.results)
            upAdapter.notifyDataSetChanged()
        })
    }
}