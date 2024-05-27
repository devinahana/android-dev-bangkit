package com.bangkit.userstory.ui.authentication.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.userstory.data.repository.UserRepository
import com.bangkit.userstory.data.local.entity.UserModel
import com.bangkit.userstory.data.remote.request.LoginRequest
import com.bangkit.userstory.data.remote.response.LoginResponse
import com.bangkit.userstory.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _response = MutableLiveData<LoginResponse>()
    val user: LiveData<LoginResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(request: LoginRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().loginUser(request)
        client.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val loggedinUser = response.body()?.loginResult?.token?.let {
                        UserModel(
                            request.email,
                            it,
                            true
                        )
                    }
                    loggedinUser?.let { saveSession(it) }
                    _response.value = response.body()
                } else {
                    try {
                        val errorResponse = response.errorBody()?.string()
                        val gson = Gson()
                        val error = gson.fromJson(errorResponse, LoginResponse::class.java)
                        Log.e(TAG, "Error logging in user: ${errorResponse.toString()}")
                        _response.value = error
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception occurred while parsing error response: ${e.message}")
                        _response.value = LoginResponse(error = true, message = "Error logging in user")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _response.value = LoginResponse(error = true, message = "Error logging in user")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun clearResponse() {
        _response.value = LoginResponse(error = null, message = null)
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }

}