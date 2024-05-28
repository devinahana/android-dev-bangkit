package com.example.userapplication.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.userapplication.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): UserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserRoomDatabase::class.java, "note_database")
                        .build()
                }
            }
            return INSTANCE as UserRoomDatabase
        }
    }
}