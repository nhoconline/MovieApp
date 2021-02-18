package trantuan.demo.movieapp.data.model.movieTop


import com.google.gson.annotations.SerializedName

data class TopMovie(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultTop>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)