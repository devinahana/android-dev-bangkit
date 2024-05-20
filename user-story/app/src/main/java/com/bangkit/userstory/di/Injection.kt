package com.bangkit.userstory.di

import android.content.Context
import com.bangkit.userstory.data.UserRepository
import com.bangkit.userstory.data.pref.UserPreference
import com.bangkit.userstory.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}