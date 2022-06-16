package com.genta.storyapp.Data.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.genta.storyapp.Data.API.Service
import com.genta.storyapp.Data.Response.ResponseGetStory
import com.genta.storyapp.Model.UserPreference
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: Service,private val userPreference: UserPreference) : PagingSource<Int, ResponseGetStory>()
{
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params : LoadParams<Int>):LoadResult<Int, ResponseGetStory> {
        return try {
            val token = userPreference.getUser().first().token
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(token, position, params.loadSize).listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResponseGetStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}