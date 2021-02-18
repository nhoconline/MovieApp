package trantuan.demo.movieapp.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trantuan.demo.movieapp.data.api.API_KEY
import trantuan.demo.movieapp.data.api.MyApi
import trantuan.demo.movieapp.data.model.movieDetail.Details
import trantuan.demo.movieapp.data.model.videoId.VideoID
import trantuan.demo.movieapp.ui.home.KEY

class DetailViewModel : ViewModel() {
    var result = MutableLiveData<Details>()
    var resultID = MutableLiveData<VideoID>()
    var error = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    fun loadDetail(id: Int){
        isLoading.value = true
        MyApi().getDetail(id).enqueue(object : Callback<Details>{
            override fun onFailure(call: Call<Details>, t: Throwable) {
                error.value = t.message
            }

            override fun onResponse(call: Call<Details>, response: Response<Details>) {
                isLoading.value = true
                result.value = response.body()
                Log.e("DataAdapter", "onResponse: ${result.value}")
            }

        })
    }


    fun loadVideo(movie_id : Int){
        MyApi().getIdVideo(movie_id).enqueue(object : Callback<VideoID>{
            override fun onFailure(call: Call<VideoID>, t: Throwable) {
                error.value = t.message
            }

            override fun onResponse(call: Call<VideoID>, response: Response<VideoID>) {
                isLoading.value = true
                resultID.value = response.body()
                Log.e("DataAdapter", "onResponse: ${resultID.value}")
            }

        })
    }


}