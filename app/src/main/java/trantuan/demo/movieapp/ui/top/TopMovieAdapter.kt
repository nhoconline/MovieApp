package trantuan.demo.movieapp.ui.top

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_top_movie.view.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.api.POSTER_BASE_URL
import trantuan.demo.movieapp.data.model.movieTop.ResultTop
import trantuan.demo.movieapp.ui.detail.DetailActivity

class TopMovieAdapter(
    private var mContext: Context?,
    var top: ArrayList<ResultTop?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    class TopViewHolder(topView: View) : RecyclerView.ViewHolder(topView) {
        val topTitle: TextView = topView.tv_title_top
        val topDate: TextView = topView.tv_date_top
        val topImg: ImageView = topView.img_poster_top
        val cardView: CardView = topView.card_view_top
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progress: ProgressBar = v.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.list_top_movie, parent, false)
            TopViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            ProgressViewHolder(itemView)
        }
    }

    override fun getItemCount() = top.size

    override fun getItemViewType(position: Int): Int {
        return if (top[position] != null) VIEW_ITEM
        else VIEW_PROG
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TopViewHolder -> {
                val item = top[position]
                holder.topTitle.text = item?.title
                holder.topDate.text = item?.releaseDate

                val iconTop = "$POSTER_BASE_URL${item?.backdropPath}"
                mContext?.let { Glide.with(it).load(iconTop).into(holder.topImg) }

                holder.cardView.setOnClickListener {
                    val intent = Intent(mContext, DetailActivity::class.java)
                    intent.putExtra("id", item?.id)
                    mContext?.startActivity(intent)
                }
            }
            is ProgressViewHolder -> {
                holder.progress.isIndeterminate = true
            }
        }
    }
}
