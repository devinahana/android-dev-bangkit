package com.bangkit.userstory.data.local.entity

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)