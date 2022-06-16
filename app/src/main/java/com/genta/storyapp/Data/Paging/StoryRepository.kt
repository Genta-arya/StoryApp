package com.genta.storyapp.Data.Paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.genta.storyapp.Data.API.Service
import com.genta.storyapp.Data.Response.ResponseGetStory
import com.genta.storyapp.Data.localdb.StoryDatabase
import com.genta.storyapp.Model.UserPreference


class StoryRepository(private val quoteDatabase: StoryDatabase, private val apiService: Service, private val userPreference: UserPreference)
{
    fun getListStory(): LiveData<PagingData<ResponseGetStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(quoteDatabase, apiService,userPreference),
            pagingSourceFactory = {
                quoteDatabase.storyDao().getAllQuote()
            }
        ).liveData
    }
}