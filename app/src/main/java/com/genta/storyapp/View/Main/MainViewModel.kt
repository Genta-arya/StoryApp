package com.genta.storyapp.View.Main


import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.genta.storyapp.Data.API.ApiConfig
import com.genta.storyapp.Data.Paging.StoryRepository
import com.genta.storyapp.Data.Response.MsgGetStory
import com.genta.storyapp.Data.Response.ResponseGetStory
import com.genta.storyapp.Model.UserModel
import com.genta.storyapp.Model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val pref: UserPreference, storyRepository: StoryRepository)  : ViewModel(){
    private val _Loading = MutableLiveData<Boolean>()
    val Loading: LiveData<Boolean> = _Loading
    private val _APIresult = MutableLiveData<String>()
    val APIresult : LiveData<String> =_APIresult
    val StoryUser: LiveData<PagingData<ResponseGetStory>> = storyRepository.getListStory().cachedIn(viewModelScope)

    val Story = MutableLiveData<List<ResponseGetStory>>()


    fun exit(){
        viewModelScope.launch {
            pref.logout()
        }
    }
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getStoryuser(token : String){
        val listStory = ArrayList<ResponseGetStory>()
        val client = ApiConfig.getApiService().getStory("Bearer "+token,1)

        client.enqueue(object: Callback<MsgGetStory>{
            override fun onResponse(call: Call<MsgGetStory>, response: Response<MsgGetStory>) {
                val _response = response.body()
                if (response.isSuccessful && _response != null){
                    Story.postValue(_response.listStory)
                    Story.value = listStory
                    Log.d("story token","${_response.listStory}")

                }else{
                    _APIresult.value = _response?.message
                }
            }

            override fun onFailure(call: Call<MsgGetStory>, t: Throwable) {
                _APIresult.value = t.message
            }
        })
    }
}