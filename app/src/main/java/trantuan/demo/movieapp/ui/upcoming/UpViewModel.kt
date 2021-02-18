package trantuan.demo.movieapp.ui.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trantuan.demo.movieapp.data.api.MyApi
import trantuan.demo.movieapp.data.model.movieUpcoming.Upcoming


class UpViewModel: ViewModel() {
//    var result : MutableLiveData<Upcoming>()
//    var error : MutableLiveData<String>()
//    var isLoading : MutableLiveData<Boolean>()


    var result = MutableLiveData<Upcoming>()
    var error = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    fun loadUp(page: Int){
        isLoading.value = true
        MyApi().getUpcoming(page.toString()).enqueue(object : Callback<Upcoming>{
            override fun onFailure(call: Call<Upcoming>, t: Throwable) {
                error.value = t.message
                isLoading.value = false
            }

            override fun onResponse(call: Call<Upcoming>, response: Response<Upcoming>) {
                isLoading.value = true
                result.value = response.body()
            }

        })
    }
}