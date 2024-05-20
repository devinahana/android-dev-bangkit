package com.bangkit.userstory.data.retrofit

import com.bangkit.userstory.data.request.*
import com.bangkit.userstory.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registerUser (
        @Body request: RegisterRequest
    ): Call<GeneralResponse>

    @POST("login")
    fun loginUser (
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): Call<GetAllStoriesResponse>

    @GET("stories/{idStory}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("idStory") idStory: String
    ): Call<GetDetailStoryReponse>

    @Multipart
    @POST("stories")
    fun createStoryAuthenticated(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<GeneralResponse>

    @Multipart
    @POST("stories/guest")
    fun createStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<GeneralResponse>
}