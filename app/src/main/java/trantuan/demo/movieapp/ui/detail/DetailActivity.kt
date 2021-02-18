@file:Suppress("DEPRECATION")

package trantuan.demo.movieapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import trantuan.demo.movieapp.Constants
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.api.POSTER_BASE_URL
import trantuan.demo.movieapp.databinding.ActivityDetailBinding
import trantuan.demo.movieapp.ui.playvideo.PlayVideoActivity

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    lateinit var videoKey: String
    private lateinit var videoId: String
    private lateinit var videoName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        binding.detailModel = viewModel

        getExtra()

        setupUI()
        subscribe()

        click()

    }

    private fun getExtra() {
        val movieId: Int = intent.getIntExtra("id", 1)
        Log.e("kevin", "movieId:$movieId ")

        viewModel.loadDetail(movieId)

        viewModel.loadVideo(movieId)
    }

    private fun setupUI() = with(binding){
        toolBar.tv_title.isSelected = true
        toolBar.iv_back.setOnClickListener {
            finish()
        }
    }
    private fun click() {
        with(binding) {
            btnPlay.setOnClickListener {
                if (videoKey.isNullOrEmpty()) {
                    Toast.makeText(
                        this@DetailActivity,
                        "Video detail is not available!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(this@DetailActivity, PlayVideoActivity::class.java)
                    intent.putExtra("key", videoKey)
                    intent.putExtra(Constants.Key.VIDEO_ID, videoId)
                    intent.putExtra(Constants.Key.VIDEO_NAME,videoName )
                    startActivity(intent)
                }
                Log.e("key", "onCreate: $intent")
            }
        }
    }


    private fun subscribe() = with(binding.detailModel!!) {
        resultID.observe(this@DetailActivity, Observer {
            if (it.results.isNotEmpty())
                videoKey = it.results[0].key
                videoId = it.id.toString()
                videoName = it.results[0].name
                binding.toolBar.tv_title.text = videoName
        })

        result.observe(this@DetailActivity, Observer {
            tv_titleDetail.text = it.originalTitle
            tv_dateDetail.text = it.releaseDate
            tv_Overview.text = it.overview
            tv_vote.text = it.voteAverage.toString() + VOTE
            val iconDetail = "$POSTER_BASE_URL${it.backdropPath}"
            Glide.with(this@DetailActivity).load(iconDetail).into(img_iconDetail)
        })

    }
}