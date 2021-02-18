package trantuan.demo.movieapp.data.model.videoId


import com.google.gson.annotations.SerializedName

data class VideoID(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<Result>
)