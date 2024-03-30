package com.example.userapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydatastore.SettingPreferences
import com.example.mydatastore.dataStore
import com.example.userapplication.R
import com.example.userapplication.data.database.User
import com.example.userapplication.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var pref: SettingPreferences
    private lateinit var modeViewModel: ModeViewModel

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.fav)
        setSupportActionBar(binding.toolbar);

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        pref = SettingPreferences.getInstance(application.dataStore)
        modeViewModel = ViewModelProvider(this, ViewModelFactory(pref = pref)).get(
            ModeViewModel::class.java
        )

        val userFactory = UserViewModelFactory.getInstance(this@FavoriteActivity.application)
        userViewModel = ViewModelProvider(this, userFactory).get(
            UserViewModel::class.java
        )

        userViewModel.getFavoriteUsers().observe(this) { users ->
            setListUserData(users)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)

        val menuItemToHide = menu.findItem(R.id.fav_user)
        menuItemToHide.isVisible = false

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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateThemeMenuItem(isDarkMode: Boolean) {
        val menuItemMode = binding.toolbar.menu.findItem(R.id.change_mode)
        val menuItemFav = binding.toolbar.menu.findItem(R.id.fav_user)
        if (isDarkMode) { // Dark mode
            menuItemMode.title = getString(R.string.dark_mode)
            menuItemMode.setIcon(R.drawable.moon_filled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else { // Light mode
            menuItemMode.title = getString(R.string.light_mode)
            menuItemMode.setIcon(R.drawable.moon_border)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setListUserData(users: List<User>) {
        val adapter = ListUserAdapter<User>()
        adapter.submitList(users)
        binding.recyclerView.adapter = adapter
    }
}