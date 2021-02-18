package trantuan.demo.movieapp.ui.upcoming

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
import kotlinx.android.synthetic.main.list_up_movie.view.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.api.POSTER_BASE_URL
import trantuan.demo.movieapp.data.model.movieUpcoming.ResultUpcoming
import trantuan.demo.movieapp.ui.detail.DetailActivity

class UpcomingAdapter(
    private var mContext: Context?,
    private var upComing: ArrayList<ResultUpcoming?>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    class UpViewHolder(UpView: View) : RecyclerView.ViewHolder(UpView) {
        val imgUpcoming: ImageView = UpView.img_poster_up
        val titleUp: TextView = UpView.tv_title_up
        val dateUp: TextView = UpView.tv_date_up
        val cardView: CardView = UpView.card_view_up
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.list_up_movie, parent, false)
            UpViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            ProgressViewHolder(itemView)
        }

    }

    override fun getItemCount() = upComing.size

    override fun getItemViewType(position: Int): Int {
        return if (upComing[position] != null) VIEW_ITEM
        else VIEW_PROG
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar = v.findViewById(R.id.progressBar)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UpViewHolder -> {
                val itemUp = upComing[position]
                holder.titleUp.text = itemUp?.title
                holder.dateUp.text = itemUp?.releaseDate

                val iconUpcoming = "$POSTER_BASE_URL${itemUp?.posterPath}"
                mContext?.let { Glide.with(it).load(iconUpcoming).into(holder.imgUpcoming) }

                holder.cardView.setOnClickListener {
                    val intent = Intent(mContext, DetailActivity::class.java)
                    intent.putExtra("id", itemUp?.id)
                    mContext?.startActivity(intent)
                }
            }
            is ProgressViewHolder -> {
                holder.progressBar.isIndeterminate = true
            }
        }
    }
}