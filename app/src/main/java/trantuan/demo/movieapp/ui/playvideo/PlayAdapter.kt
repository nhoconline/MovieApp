package trantuan.demo.movieapp.ui.playvideo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_play_video.view.*
import kotlinx.android.synthetic.main.list_item_play.view.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.api.POSTER_BASE_URL
import trantuan.demo.movieapp.data.model.movieHome.Movies
import trantuan.demo.movieapp.data.model.movieSimilar.ResultResponse
import trantuan.demo.movieapp.ui.detail.DetailActivity

class PlayAdapter(private var mContext : Context,var play : List<ResultResponse?>, private val callBack: (ResultResponse) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    class PlayVieHolder(playView : View) : RecyclerView.ViewHolder(playView) {
        val mTitle : TextView = playView.tv_titlePlay
        val mDate : TextView = playView.tv_datePlay
        val mVote : TextView = playView.tv_votePlay
//        val mRecyclerView : RecyclerView = playView.rv_list_play
        val mIcon : ImageView = playView.img_video
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progress: ProgressBar = v.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return if (viewType == VIEW_ITEM) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_play, parent, false)
            PlayVieHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            ProgressViewHolder(itemView)
        }
    }

    override fun getItemCount() = play.size

    override fun getItemViewType(position: Int): Int {
        return if (play[position] != null) VIEW_ITEM
        else VIEW_PROG
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PlayVieHolder -> {
                val item = play[position]
                holder.mTitle.text = item?.title
                holder.mDate.text = item?.releaseDate
                holder.mVote.text = item?.voteCount.toString()

                val iconTop = "$POSTER_BASE_URL${item?.backdropPath}"
                mContext?.let { Glide.with(it).load(iconTop).into(holder.mIcon) }

//                holder.mRecyclerView.setOnClickListener {
//                    val intent = Intent(mContext, DetailActivity::class.java)
//                    intent.putExtra("id", item?.id)
//                    mContext?.startActivity(intent)
//                }

                holder.itemView.setOnClickListener {
                    if (item != null) {
                        callBack.invoke(item)
                    }
                }
            }
            is ProgressViewHolder -> {
                holder.progress.isIndeterminate = true
            }
        }
    }
}