package trantuan.demo.movieapp.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_detail.view.*
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.data.api.POSTER_BASE_URL
import trantuan.demo.movieapp.data.model.movieDetail.Details

class DetailAdapter(var mContext : Context?, var details : List<Details>, private val callBack : (Details) -> Unit)
    : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    class ViewHolder(detailView: View) :  RecyclerView.ViewHolder(detailView) {
        val detailTitle : TextView = detailView.tv_titleDetail
        val detailDate : TextView = detailView.tv_dateDetail
        val detailOverview : TextView = detailView.tv_Overview
        val detailVote : TextView = detailView.tv_vote
        val detailPoster : ImageView = detailView.img_iconDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val detailView = LayoutInflater.from(parent.context).inflate(R.layout.list_detail, parent, false)
        return ViewHolder(detailView)
    }

    override fun getItemCount() = details.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = details[position]
        holder.detailTitle.text = item.title
        holder.detailDate.text = item.releaseDate
        holder.detailOverview.text = item.overview
        holder.detailVote.text = item.voteAverage.toString()

        val icon = "$POSTER_BASE_URL${item.backdropPath}"
        mContext?.let { Glide.with(it).load(icon).into(holder.detailPoster) }

        holder.itemView.setOnClickListener {
            callBack.invoke(item)
        }
    }
}