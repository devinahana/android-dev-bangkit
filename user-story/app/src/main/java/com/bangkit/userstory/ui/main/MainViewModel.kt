package com.bangkit.userstory.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.userstory.data.repository.UserRepository
import com.bangkit.userstory.data.local.entity.UserModel
import com.bangkit.userstory.data.remote.response.GeneralResponse
import com.bangkit.userstory.data.remote.response.GetAllStoriesResponse
import com.bangkit.userstory.data.remote.response.GetDetailStoryReponse
import com.bangkit.userstory.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _listStory = MutableLiveData<GetAllStoriesResponse>()
    val listStory: LiveData<GetAllStoriesResponse> = _listStory

    private val _detailStory = MutableLiveData<GetDetailStoryReponse>()
    val detailStory: LiveData<GetDetailStoryReponse> = _detailStory

    private val _newStory = MutableLiveData<GeneralResponse>()
    val newStory: LiveData<GeneralResponse> = _newStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStories("Bearer " + token)
        client.enqueue(object : Callback<GetAllStoriesResponse> {

            override fun onResponse(call: Call<GetAllStoriesResponse>, response: Response<GetAllStoriesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStory.value = response.body()
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                        val gson = Gson()
                        val error = gson.fromJson(errorResponse, GetAllStoriesResponse::class.java)
                        Log.e(TAG, "Error fetching stories: ${errorResponse.toString()}")
                        _listStory.value = error
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception occurred while parsing error response: ${e.message}")
                        _listStory.value = GetAllStoriesResponse(error = true, message = "Error fetching stories")
                    }
                }
            }

            override fun onFailure(call: Call<GetAllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                _listStory.value = GetAllStoriesResponse(error = true, message = "Error fetching stories")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailStory(token: String, idStory: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailStory("Bearer " + token, idStory)
        client.enqueue(object : Callback<GetDetailStoryReponse> {

            override fun onResponse(call: Call<GetDetailStoryReponse>, response: Response<GetDetailStoryReponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailStory.value = response.body()
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                        val gson = Gson()
                        val error = gson.fromJson(errorResponse, GetDetailStoryReponse::class.java)
                        Log.e(TAG, "Error fetching detail story: ${errorResponse.toString()}")
                        _detailStory.value = error
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception occurred while parsing error response: ${e.message}")
                        _detailStory.value = GetDetailStoryReponse(error = true, message = "Error fetching detail story")
                    }
                }
            }

            override fun onFailure(call: Call<GetDetailStoryReponse>, t: Throwable) {
                _isLoading.value = false
                _detailStory.value = GetDetailStoryReponse(error = true, message = "Error fetching detail story")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun createStory(token: String, imageFile: File, description: String) {
        _isLoading.value = true
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/*".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().createStoryAuthenticated("Bearer " + token, multipartBody, requestBody)
        client.enqueue(object : Callback<GeneralResponse> {

            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _newStory.value = response.body()
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                        val gson = Gson()
                        val error = gson.fromJson(errorResponse, GeneralResponse::class.java)
                        Log.e(TAG, "Error creating new story: ${errorResponse.toString()}")
                        _newStory.value = error
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception occurred while parsing error response: ${e.message}")
                        _newStory.value = GeneralResponse(error = true, message = "Error creating new story")
                    }
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _isLoading.value = false
                _newStory.value = GeneralResponse(error = true, message = "Error creating new story")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun clearResponse() {
        _newStory.value = GeneralResponse(error = null, message = null)
    }

    companion object{
        private const val TAG = "MainViewModel"
    }

}