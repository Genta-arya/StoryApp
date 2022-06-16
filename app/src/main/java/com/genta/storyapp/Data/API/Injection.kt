package com.genta.storyapp.Data.API

import android.content.Context
import com.genta.storyapp.Data.Paging.StoryRepository
import com.genta.storyapp.Data.localdb.StoryDatabase
import com.genta.storyapp.Model.UserPreference

object Injection {
    fun provideRepository(context: Context,userPreference: UserPreference): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService,userPreference)
    }
}