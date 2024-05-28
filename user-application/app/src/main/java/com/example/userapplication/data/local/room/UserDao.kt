package com.example.userapplication.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.userapplication.data.local.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("UPDATE user_table SET isFavorite = :isFavorite WHERE username = :username")
    fun setFavorite(username: String, isFavorite: Boolean)

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUser(username: String): LiveData<User>

    @Query("SELECT * FROM user_table ORDER BY username ASC")
    fun getAllUser(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE isFavorite = 1 ORDER BY username ASC")
    fun getFavoriteUsers(): LiveData<List<User>>
}