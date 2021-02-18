package trantuan.demo.movieapp.data.model.movieHome


import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String
)