package trantuan.demo.movieapp.data.model.movieHome


import com.google.gson.annotations.SerializedName

data class HomeMovieData(
    @SerializedName("page")
    val page: String,
    @SerializedName("results")
    val results: List<Movies>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)