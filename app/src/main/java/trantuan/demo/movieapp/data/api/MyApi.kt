package trantuan.demo.movieapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import trantuan.demo.movieapp.data.model.movieDetail.Details
import trantuan.demo.movieapp.data.model.movieHome.HomeMovieData
import trantuan.demo.movieapp.data.model.movieSimilar.MovieSimilar
import trantuan.demo.movieapp.data.model.movieTop.TopMovie
import trantuan.demo.movieapp.data.model.movieUpcoming.Upcoming
import trantuan.demo.movieapp.data.model.videoId.VideoID


const val API_KEY = "254f2818f84123885f9ecd1f160b1bb9"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

interface MyApi {
    @GET("movie/popular")
    fun getAllMovie(
        @Query("page") page: String
    ) : Call<HomeMovieData>

    @GET("movie/{movie_id}")
    fun getDetail(
        @Path("movie_id") id: Int
    ) : Call<Details>

    @GET("movie/top_rated")
    fun getTop(
        @Query("page") pageTop : String
    ) : Call<TopMovie>

    @GET("movie/upcoming")
    fun getUpcoming(
        @Query("page") pageUp : String
    ) : Call<Upcoming>

    @GET("movie/{movie_id}/videos")
    fun getIdVideo(
        @Path("movie_id") movie_id : Int
    ) : Call<VideoID>

    @GET("movie/{movie_id}/similar")
    fun getSimilar(
        @Path("movie_id") movie_id: Int,
        @Query("page") page: Int
    ) : Call<MovieSimilar>

    companion object{
        operator fun invoke() : MyApi{
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", "$API_KEY")
                    .build()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(MyApi::class.java)
        }
    }
}