package com.genta.storyapp.dao
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genta.storyapp.Data.Response.ResponseGetStory


@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<ResponseGetStory>)

    @Query("SELECT * FROM story")
    fun getAllQuote(): PagingSource<Int, ResponseGetStory>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}