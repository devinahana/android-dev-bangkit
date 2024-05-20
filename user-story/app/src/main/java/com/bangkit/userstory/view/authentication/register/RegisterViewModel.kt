package com.bangkit.userstory.view.authentication.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.userstory.data.request.RegisterRequest
import com.bangkit.userstory.data.response.GeneralResponse
import com.bangkit.userstory.data.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel() : ViewModel() {
    private val _response = MutableLiveData<GeneralResponse>()
    val user: LiveData<GeneralResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(request: RegisterRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerUser(request)
        client.enqueue(object : Callback<GeneralResponse> {

            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = response.body()
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                        val gson = Gson()
                        val error = gson.fromJson(errorResponse, GeneralResponse::class.java)
                        _response.value = error
                        Log.e(TAG, "Error creating new user: ${errorResponse.toString()}")
                    } catch (e: Exception) {
                        _response.value = GeneralResponse(error = true, message = "Error creating new user")
                        Log.e(TAG, "Exception occurred while parsing error response: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _isLoading.value = false
                _response.value = GeneralResponse(error = true, message = "Error creating new user")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun clearResponse() {
        _response.value = GeneralResponse(error = null, message = null)
    }

    companion object{
        private const val TAG = "RegisterViewModel"
    }
}