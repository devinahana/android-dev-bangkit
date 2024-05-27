package com.bangkit.userstory.di

import android.content.Context
import com.bangkit.userstory.data.repository.UserRepository
import com.bangkit.userstory.data.local.pref.UserPreference
import com.bangkit.userstory.data.local.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}