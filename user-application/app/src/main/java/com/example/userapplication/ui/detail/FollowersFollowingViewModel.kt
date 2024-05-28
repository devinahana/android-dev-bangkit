package com.example.userapplication.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userapplication.data.remote.response.UserResponse
import com.example.userapplication.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFollowingViewModel(private val username: String) : ViewModel() {
    private val _listFollowers = MutableLiveData<List<UserResponse>>()
    val listFollowers: LiveData<List<UserResponse>> = _listFollowers

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _listFollowing = MutableLiveData<List<UserResponse>>()
    val listFollowing: LiveData<List<UserResponse>> = _listFollowing

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    init {
        getListFollowers(username)
        getListFollowing(username)
    }

    fun getListFollowers(username: String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getFollowersList(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: Error when fetching list user")
                }
            }
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getListFollowing(username: String) {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getFollowingList(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: Error when fetching list user")
                }
            }
            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "FragmentViewModel"
    }


}