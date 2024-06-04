package com.bangkit.userstory.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.userstory.data.remote.response.Story
import com.bangkit.userstory.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStories("Bearer $token", page, params.loadSize, location = 1)
            val responseData = response.listStory!!

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}