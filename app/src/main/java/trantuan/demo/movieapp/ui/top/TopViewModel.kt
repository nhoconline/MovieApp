package trantuan.demo.movieapp.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trantuan.demo.movieapp.data.api.MyApi
import trantuan.demo.movieapp.data.model.movieTop.TopMovie

class TopViewModel : ViewModel() {

    var result = MutableLiveData<TopMovie>()
    var error = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    fun loadTop(page : Int) {
        isLoading.value = true
        MyApi().getTop(page.toString()).enqueue(object : Callback<TopMovie>{
            override fun onFailure(call: Call<TopMovie>, t: Throwable) {
                error.value = t.message
                isLoading.value = false
            }

            override fun onResponse(call: Call<TopMovie>, response: Response<TopMovie>) {
                isLoading.value = true
                result.value = response.body()
            }

        })
    }

}