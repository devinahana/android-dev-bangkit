package com.example.userapplication.data.remote.retrofit

import com.example.userapplication.data.remote.response.SearchResponse
import com.example.userapplication.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getUserList (
    ): Call<List<UserResponse>>

    @GET("search/users")
    fun searchUserList (
        @Query("q") q: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    fun getFollowersList (
        @Path("username") username: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getFollowingList (
        @Path("username") username: String
    ): Call<List<UserResponse>>
}