package com.example.userapplication.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.userapplication.data.database.User
import com.example.userapplication.data.database.UserRepository

class UserViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    lateinit var user: LiveData<User>

    fun insert(user: User) {
        mUserRepository.insert(user)
    }
    fun setFavorite(username: String, isFavorite: Boolean) {
        mUserRepository.setFavorite(username, isFavorite)
    }
    fun getUser(username: String): LiveData<User> = mUserRepository.getUser(username)

    fun getFavoriteUsers(): LiveData<List<User>> = mUserRepository.getFavoriteUsers()
}