package com.bangkit.userstory.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.userstory.data.local.room.StoryDatabase
import com.bangkit.userstory.data.paging.StoryRemoteMediator
import com.bangkit.userstory.data.remote.response.Story
import com.bangkit.userstory.data.remote.retrofit.ApiService

class StoryPagingRepository private constructor(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryPagingRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): StoryPagingRepository =
            instance ?: synchronized(this) {
                instance ?: StoryPagingRepository(storyDatabase, apiService)
            }.also { instance = it }
    }
}