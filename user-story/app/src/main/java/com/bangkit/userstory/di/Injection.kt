package com.bangkit.userstory.di

import android.content.Context
import com.bangkit.userstory.data.repository.UserRepository
import com.bangkit.userstory.data.local.pref.UserPreference
import com.bangkit.userstory.data.local.pref.dataStore
import com.bangkit.userstory.data.local.room.StoryDatabase
import com.bangkit.userstory.data.remote.retrofit.ApiConfig
import com.bangkit.userstory.data.repository.StoryPagingRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideStoryRepository(context: Context): StoryPagingRepository {
        val storyDatabase = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryPagingRepository.getInstance(storyDatabase, apiService)
    }

}