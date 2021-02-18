package trantuan.demo.movieapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trantuan.demo.movieapp.data.api.MyApi
import trantuan.demo.movieapp.data.model.movieHome.HomeMovieData

class HomeViewModel : ViewModel() {
    var result = MutableLiveData<HomeMovieData>()

    val error = MutableLiveData<String>()

    var isLoading = MutableLiveData<Boolean>()

    fun loadData(page: Int){

        isLoading.value = true
        MyApi().getAllMovie(page.toString()).enqueue(object : Callback<HomeMovieData> {
            override fun onFailure(call: Call<HomeMovieData>, t: Throwable) {
                error.value = t.message
                isLoading.value = false
            }
            override fun onResponse(call: Call<HomeMovieData>, response: Response<HomeMovieData>) {
                isLoading.value = false
                if (response.isSuccessful){
                    result.value = response.body()
                    Log.e("log", result.value.toString())
                }else{
                    error.value = response.message()
                }
            }
        })
    }
}
