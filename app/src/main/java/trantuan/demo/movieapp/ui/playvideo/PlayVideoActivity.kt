package trantuan.demo.movieapp.ui.playvideo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import trantuan.demo.movieapp.Constants
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.model.movieSimilar.ResultResponse
import trantuan.demo.movieapp.databinding.ActivityPlayVideoBinding
import trantuan.demo.movieapp.ui.detail.DetailActivity
import trantuan.demo.movieapp.ui.home.HomeAdapter


@Suppress("DEPRECATION")
class PlayVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
    LifecycleOwner {
    //    private var VIDEO = "sAdNmVEHZvQ" //"SR6iYWJxHqs";
    private lateinit var binding: ActivityPlayVideoBinding
    private lateinit var lifecycleRegistry: LifecycleRegistry
    private lateinit var videoKey: String
    private lateinit var videoId: String
    private lateinit var videoName: String

    private var currentPage = 1
    private var totalPage = 1
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount: Int = 0

    private lateinit var viewModel: PlayViewModel
    private lateinit var playAdapter: PlayAdapter
    private var listPlay: ArrayList<ResultResponse?> = ArrayList()

    private var isLoading = false
    private var isRefresh = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_video)

        viewModel = getViewModelProvider()!!.get(PlayViewModel::class.java)

        binding.playViewModel = viewModel


        playAdapter = PlayAdapter(this@PlayVideoActivity, listPlay) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)

            Log.e("int", "onCreateView: ${it.id}")
        }

        initViews()
        initData()
        subscriber()
        initControl()
        initScrollListener()
    }

    private fun initViews() = with(binding) {

        val youTubeView = findViewById<View>(R.id.youtube_view) as YouTubePlayerView

        youTubeView.initialize(DEVELOPER_KEY, this@PlayVideoActivity)


        rvListPlay.layoutManager = LinearLayoutManager(this@PlayVideoActivity)
        rvListPlay.adapter = playAdapter
    }

    private fun initData() = with(viewModel) {
        intent?.apply {
            videoKey = getStringExtra("key") ?: ""
            videoId = getStringExtra(Constants.Key.VIDEO_ID) ?: ""
            videoName = getStringExtra(Constants.Key.VIDEO_NAME) ?: ""
        }
        binding.toolBar.tv_title.text = videoName
        viewModel.loadSimilar(videoId.toInt(), currentPage)
    }

    private fun initControl() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            // refresh your list contents somehow
            refresh()
            swipeRefresh.isRefreshing =
                false               // reset the SwipeRefreshLayout (stop the loading spinner)
        }
        toolBar.iv_back.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        p2: Boolean
    ) {
        youTubePlayer?.cueVideo(videoKey)
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "Error :" + youTubeInitializationResult.toString(), Toast.LENGTH_LONG)
            .show()
    }

    private fun initScrollListener() {
        binding.rvListPlay.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        listPlay.add(null)
        playAdapter.notifyItemInserted(listPlay.size - 1)
        viewModel.loadSimilar(videoId.toInt(), currentPage)
    }

    private fun refresh() {
        isRefresh = true
        listPlay.clear()
        currentPage = 1
        this.isLoading = false
        viewModel.loadSimilar(videoId.toInt(), currentPage)

    }

    private fun subscriber() = with(binding.playViewModel!!) {
        resul.observe(this@PlayVideoActivity, Observer {
            totalPage = it.totalPages
            if (isRefresh) {
                listPlay.clear()
                isRefresh = false
            }
            if (this@PlayVideoActivity.isLoading) {
                listPlay.removeAt(listPlay.size - 1)
                val scrollPosition: Int = listPlay.size
                playAdapter.notifyItemRemoved(scrollPosition)
                this@PlayVideoActivity.isLoading = false
            }
            listPlay.addAll(it.results)
            playAdapter.notifyDataSetChanged()
        })
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    @Nullable
    private var viewModelStore: ViewModelStore? = null

    override fun onRetainNonConfigurationInstance(): Any? {
        return viewModelStore
    }

    private fun getViewModelStore(): ViewModelStore {
        val nonConfigurationInstance = lastNonConfigurationInstance
        if (nonConfigurationInstance is ViewModelStore) {
            viewModelStore = nonConfigurationInstance
        }
        if (viewModelStore == null) {
            viewModelStore = ViewModelStore()
        }
        return viewModelStore!!
    }

    private fun getViewModelProvider(): ViewModelProvider? {
        val factory: ViewModelProvider.Factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        return ViewModelProvider(getViewModelStore(), factory)
    }

    companion object {
        const val DEVELOPER_KEY = "AIzaSyB5Jgo4jTYPq5Nep-7k2KqQCHjV4wbWC-w"
    }

}