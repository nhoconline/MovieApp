package trantuan.demo.movieapp.ui.playvideo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response
import trantuan.demo.movieapp.data.api.MyApi
import trantuan.demo.movieapp.data.model.movieSimilar.MovieSimilar

class PlayViewModel: ViewModel() {
    val resul = MutableLiveData<MovieSimilar>()
    val error = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun loadSimilar(movie_id : Int, page: Int){
        isLoading.value = true
        MyApi().getSimilar(movie_id, page).enqueue(object : retrofit2.Callback<MovieSimilar> {
            override fun onFailure(call: Call<MovieSimilar>, t: Throwable) {
                error.value = t.message
            }

            override fun onResponse(call: Call<MovieSimilar>, response: Response<MovieSimilar>) {
                isLoading.value = true
                resul.value = response.body()
            }

        })
    }
}