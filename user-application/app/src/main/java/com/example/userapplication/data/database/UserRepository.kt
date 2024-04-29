package com.example.userapplication.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }
    fun insert(user: User) {
        executorService.execute { mUserDao.insert(user) }
    }
    fun setFavorite(username: String, isFavorite: Boolean) {
        executorService.execute { mUserDao.setFavorite(username, isFavorite) }
    }
    fun getUser(username: String): LiveData<User> = mUserDao.getUser(username)
    fun getAllUser(): LiveData<List<User>> = mUserDao.getAllUser()
    fun getFavoriteUsers(): LiveData<List<User>> = mUserDao.getFavoriteUsers()
}