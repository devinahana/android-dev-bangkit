package com.example.userapplication.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mydatastore.SettingPreferences
import com.example.mydatastore.dataStore
import com.example.userapplication.R
import com.example.userapplication.data.local.entity.User
import com.example.userapplication.data.remote.response.UserResponse
import com.example.userapplication.databinding.ActivityDetailBinding
import com.example.userapplication.ui.favorite.FavoriteActivity
import com.example.userapplication.ui.ModeViewModel
import com.example.userapplication.views.SectionsPagerAdapter
import com.example.userapplication.ui.UserViewModel
import com.example.userapplication.utils.UserViewModelFactory
import com.example.userapplication.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var pref: SettingPreferences
    private lateinit var modeViewModel: ModeViewModel

    private lateinit var detailViewModel : DetailViewModel

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.detail_activity_title)
        setSupportActionBar(binding.toolbar)

        pref = SettingPreferences.getInstance(application.dataStore)
        modeViewModel = ViewModelProvider(this, ViewModelFactory(pref = pref)).get(
            ModeViewModel::class.java
        )

        val userFactory = UserViewModelFactory.getInstance(this@DetailActivity.application)
        userViewModel = ViewModelProvider(this, userFactory).get(
            UserViewModel::class.java
        )

        val username = intent.getStringExtra(KEY_USERNAME)
        val factory = username?.let { ViewModelFactory(username = it) }
        detailViewModel = ViewModelProvider(this, factory!!).get(DetailViewModel::class.java)

        detailViewModel.user.observe(this) { user ->
            setUserData(user)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.userName = username ?: ""
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabFavorite.setOnClickListener {
            // Add to favorite
            if (binding.fabFavorite.contentDescription.toString() == getString(R.string.fab_fav)) {
                userViewModel.setFavorite(username, true)
                changeFavorite(true)
            }
            // Remove from favorite
            else {
                userViewModel.setFavorite(username, false)
                changeFavorite(false)
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
                val moveIntent = Intent(this@DetailActivity, FavoriteActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUserData(user: UserResponse) {
        binding.tvUsernameDetail.text = user.login
        binding.tvNameDetail.text = user.name
        binding.tvTotalFollowers.text = "${user.followers} followers"
        binding.tvTotalFollowing.text = "${user.following} following"
        Glide.with(binding.root)
            .load(user.avatarUrl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(binding.imgProfileDetail)

        // Observe the LiveData user object
        userViewModel.getUser(user.login).observe(this) { userDb ->
            if (userDb != null) {
                // User exists in the database
                val isFavoriteDb = userDb.isFavorite
                changeFavorite(isFavoriteDb)
            } else {
                // User doesn't exist in the database, insert it
                userViewModel.insert(User(user.login, user.avatarUrl))
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun updateThemeMenuItem(isDarkMode: Boolean) {
        val menuItemMode = binding.toolbar.menu.findItem(R.id.change_mode)
        val menuItemFav = binding.toolbar.menu.findItem(R.id.fav_user)
        val tabLayout = binding.tabs
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

    private fun changeFavorite(isFavorite: Boolean) {

        val username = binding.tvUsernameDetail.text.toString()

        // Add to favorite
        if (isFavorite) {
            binding.fabFavorite.contentDescription = getString(R.string.fab_unfav)
            binding.fabFavorite.setImageResource(R.drawable.heart_filled)
        }
        // Remove from favorite
        else {
            binding.fabFavorite.contentDescription = getString(R.string.fab_fav)
            binding.fabFavorite.setImageResource(R.drawable.heart_border)
        }
    }

    companion object {
        const val KEY_USERNAME = "key_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
