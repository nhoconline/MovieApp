package trantuan.demo.movieapp.data.model.movieSimilar


import com.google.gson.annotations.SerializedName

data class MovieSimilar(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultResponse>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)