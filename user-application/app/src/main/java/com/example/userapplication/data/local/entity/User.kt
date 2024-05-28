package com.example.userapplication.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class User (
    @PrimaryKey
    var username: String,

    var avatarUrl: String,

    var isFavorite: Boolean = false

): Parcelable