package com.genta.storyapp.View.Maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.genta.storyapp.Data.API.ApiConfig
import com.genta.storyapp.Data.Response.MsgGetStory
import com.genta.storyapp.Data.Response.ResponseGetStory
import com.genta.storyapp.Model.UserModel
import com.genta.storyapp.Model.UserPreference

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val pref: UserPreference):ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _APImessage = MutableLiveData<String>()
    val APImessage: LiveData<String> = _APImessage

    private val _listUser = MutableLiveData<List<ResponseGetStory>>()
    val listStory : LiveData<List<ResponseGetStory>> get() = _listUser

    fun getAllDataLocation(token:String){



            val storyList = ArrayList<ResponseGetStory>()

            val client = ApiConfig.getApiService().getStory("Bearer "+token,1)

            _isLoading.value = true
            client.enqueue(object : Callback<MsgGetStory>{
                override fun onResponse(call: Call<MsgGetStory>, response: Response<MsgGetStory>) {
                    val responseBody = response.body()
                    if(response.isSuccessful && responseBody != null){
                        Log.d("MapsActivity",responseBody.toString())
                        _listUser.postValue(responseBody.listStory)
                        _listUser.value = storyList
                    }else{
                        _isLoading.value = false
                        _APImessage.value = responseBody?.message
                        Log.d("MapsActivity","onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MsgGetStory>, t: Throwable) {
                    _isLoading.value = false
                    _APImessage.value = t.message
                    Log.e("MapsActivity", "onFailure: ${t.message}")
                }
            })

    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}