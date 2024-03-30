package com.example.userapplication.ui

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
import com.example.userapplication.data.response.UserResponse
import com.example.userapplication.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var pref: SettingPreferences
    private lateinit var modeViewModel: ModeViewModel

    private lateinit var detailViewModel : DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.detail_activity_title)
        setSupportActionBar(binding.toolbar)

        pref = SettingPreferences.getInstance(application.dataStore)
        modeViewModel = ViewModelProvider(this, ModeViewModelFactory(pref)).get(
            ModeViewModel::class.java
        )

        val username = intent.getStringExtra(KEY_USERNAME)
        val factory = username?.let { ViewModelFactory(it) }
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
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun updateThemeMenuItem(isDarkMode: Boolean) {
        val menuItem = binding.toolbar.menu.findItem(R.id.change_mode)
        if (isDarkMode) { // Dark mode
            menuItem.title = getString(R.string.dark_mode)
            menuItem.setIcon(R.drawable.moon_filled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else { // Light mode
            menuItem.title = getString(R.string.light_mode)
            menuItem.setIcon(R.drawable.moon_border)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
