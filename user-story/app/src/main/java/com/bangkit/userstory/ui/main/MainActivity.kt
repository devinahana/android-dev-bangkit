package com.bangkit.userstory.ui.main

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.userstory.R
import com.bangkit.userstory.ViewModelFactory
import com.bangkit.userstory.databinding.ActivityMainBinding
import com.bangkit.userstory.ui.authentication.welcome.WelcomeActivity
import com.bangkit.userstory.ui.main.create.CreateActivity
import com.bangkit.userstory.ui.main.maps.MapsActivity
import com.bangkit.userstory.ui.resource.ListStoryAdapter
import com.bangkit.userstory.ui.resource.LoadingStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAdapter()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                viewModel.getPagingStories(user.token).observe(this) { pagingData ->
                    binding.recyclerView.isVisible = true
                    adapter.submitData(lifecycle, pagingData)
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fabCreateStory.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
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

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ListStoryAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}