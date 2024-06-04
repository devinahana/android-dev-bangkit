package com.bangkit.userstory.data.remote.retrofit

import com.bangkit.userstory.data.remote.request.LoginRequest
import com.bangkit.userstory.data.remote.request.RegisterRequest
import com.bangkit.userstory.data.remote.response.GeneralResponse
import com.bangkit.userstory.data.remote.response.GetAllStoriesResponse
import com.bangkit.userstory.data.remote.response.GetDetailStoryReponse
import com.bangkit.userstory.data.remote.response.LoginResponse
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
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): GetAllStoriesResponse

    @GET("stories")
    fun getAllStoriesNoSuspend(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
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
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ): Call<GeneralResponse>

}