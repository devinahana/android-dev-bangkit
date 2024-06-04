package com.bangkit.userstory.ui.main.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bangkit.userstory.R
import com.bangkit.userstory.ViewModelFactory
import com.bangkit.userstory.databinding.ActivityDetailBinding
import com.bangkit.userstory.utils.formatDateTime
import com.bangkit.userstory.ui.main.MainViewModel
import com.bangkit.userstory.ui.authentication.welcome.WelcomeActivity
import com.bangkit.userstory.ui.main.maps.MapsActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                intent.getStringExtra(KEY_ID)?.let { viewModel.getDetailStory(user.token, it) }
            }
        }

        viewModel.detailStory.observe(this) {response ->
            if (response.story != null && response.error == false) {
                binding.errorTextView.isVisible = false

                binding.tvStoryOwner.text = response.story.name
                binding.tvStoryDescription.text = response.story.description
                val formattedCreatedAt = getString(R.string.createdAt) + " " + response.story.createdAt?.let {
                    formatDateTime(
                        it
                    )
                }
                binding.tvStoryCreatedAt.text = formattedCreatedAt
                Glide.with(binding.root)
                    .load(response.story.photoUrl)
                    .into(binding.imgStoryPhoto)
                binding.contentScrollView.isVisible = true
            } else {
                binding.errorTextView.text = response.message
                binding.errorTextView.isVisible = true
                binding.contentScrollView.isVisible = false
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                showLogoutConfirmationDialog()
            }
            R.id.change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    companion object {
        const val KEY_ID = "key_id"
    }
}