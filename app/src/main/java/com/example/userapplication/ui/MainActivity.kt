package com.example.userapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapplication.R
import com.example.userapplication.data.response.UserResponse
import com.example.userapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // ViewModel initialization with KTX
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.main_activity_title)
        setSupportActionBar(binding.toolbar);

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)


        mainViewModel.listUser.observe(this) { users ->
            setListUserData(users)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    val query = searchView.text.toString().trim()
                    if (query.isEmpty()) {
                        mainViewModel.getListUser()
                    } else {
                        mainViewModel.searchListUser(query)
                    }
                    searchView.hide()
                    false
                }
        }

    }

    private fun setListUserData(user: List<UserResponse>) {
        val adapter = ListUserAdapter()
        adapter.submitList(user)
        binding.recyclerView.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.searchBar.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.searchBar.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }


}