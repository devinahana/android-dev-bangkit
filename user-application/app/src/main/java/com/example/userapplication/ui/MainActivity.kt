package com.example.userapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydatastore.SettingPreferences
import com.example.mydatastore.dataStore
import com.example.userapplication.R
import com.example.userapplication.data.response.UserResponse
import com.example.userapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var pref: SettingPreferences
    private lateinit var modeViewModel: ModeViewModel

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

        pref = SettingPreferences.getInstance(application.dataStore)
        modeViewModel = ViewModelProvider(this, ViewModelFactory(pref = pref)).get(
            ModeViewModel::class.java
        )

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        modeViewModel.getThemeSettings().observe(this) { isDarkMode ->
            updateThemeMenuItem(isDarkMode) // Update menu item based on theme
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.change_mode -> {
                if (item.title == "Dark Mode") {
                    modeViewModel.saveThemeSetting(false);
                } else {
                    modeViewModel.saveThemeSetting(true);
                }
            }
            R.id.fav_user -> {
                val moveIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListUserData(users: List<UserResponse>) {
        val adapter = ListUserAdapter<UserResponse>()
        adapter.submitList(users)
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

    private fun updateThemeMenuItem(isDarkMode: Boolean) {
        val menuItemMode = binding.toolbar.menu.findItem(R.id.change_mode)
        val menuItemFav = binding.toolbar.menu.findItem(R.id.fav_user)
        if (isDarkMode) { // Dark mode
            menuItemFav.setIcon(R.drawable.heart_filled)
            menuItemMode.title = getString(R.string.dark_mode)
            menuItemMode.setIcon(R.drawable.moon_filled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else { // Light mode
            menuItemFav.setIcon(R.drawable.heart_border)
            menuItemMode.title = getString(R.string.light_mode)
            menuItemMode.setIcon(R.drawable.moon_border)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }



}