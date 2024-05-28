package com.dicoding.asclepius.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.response.Articles
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel() : ViewModel() {
    private val _listArticles = MutableLiveData<List<ArticlesItem>>()
    val listArticles: MutableLiveData<List<ArticlesItem>> = _listArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getListArticle()
    }

    private fun getListArticle() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getArticleList()
        client.enqueue(object : Callback<Articles> {

            override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    articles?.let {
                        val filteredArticles = filterArticles(it as List<ArticlesItem>)
                        _listArticles.value = filteredArticles
                    }
                } else {
                    Log.e(TAG, "onFailure: Error when fetching list article")
                }
            }

            override fun onFailure(call: Call<Articles>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun filterArticles(articles: List<ArticlesItem>): List<ArticlesItem> {
        return articles.filter { !it.title.equals("[removed]", ignoreCase = true) }
    }

    companion object{
        private const val TAG = "ArticleViewModel"
    }
}