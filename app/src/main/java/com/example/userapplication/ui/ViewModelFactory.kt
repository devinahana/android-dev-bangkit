package com.example.userapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FollowersFollowingViewModel::class.java) -> {
                FollowersFollowingViewModel(username) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(username) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}