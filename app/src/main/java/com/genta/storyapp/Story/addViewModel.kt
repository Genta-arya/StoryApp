package com.genta.storyapp.Story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.genta.storyapp.Model.UserModel
import com.genta.storyapp.Model.UserPreference

class addViewModel(private val pref: UserPreference) : ViewModel() {
    fun _getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}