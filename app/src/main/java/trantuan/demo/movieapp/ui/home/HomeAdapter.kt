package trantuan.demo.movieapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_home.view.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.api.POSTER_BASE_URL
import trantuan.demo.movieapp.data.model.movieHome.Movies

open class HomeAdapter(
    private var mContext: Context?,
    var home: List<Movies?> = arrayListOf(),
    private val callBack: (Movies) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_DATA = 0
    private val TYPE_PROGRESS = 1

    class ItemViewHolder(homeView: View) : RecyclerView.ViewHolder(homeView) {
        val img: ImageView = homeView.img_poster
        val title: TextView = homeView.tv_movieTitle
        val date: TextView = homeView.tv_date
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar = v.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATA -> {
                val homeView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_home, parent, false)
                ItemViewHolder(homeView)
            }
            TYPE_PROGRESS -> {
                val homeView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading, parent, false)
                ProgressViewHolder(homeView)
            }
            else -> throw IllegalArgumentException("View Type")
        }
    }

    override fun getItemCount(): Int {
        return home.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (home[position] != null) TYPE_DATA
        else TYPE_PROGRESS
    }

    fun updateListItem(list: ArrayList<Movies?>) {
        home = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val item = home[position]
                holder.title.text = item?.title
                holder.date.text = item?.releaseDate

                val iconPorter = "$POSTER_BASE_URL${item?.backdropPath}"
                mContext?.let { Glide.with(it).load(iconPorter).into(holder.img) }

                holder.itemView.setOnClickListener {
                    if (item != null) {
                        callBack.invoke(item)
                    }
                }
            }
            is ProgressViewHolder -> {
                holder.progressBar.isIndeterminate = true
            }
        }
    }
}
