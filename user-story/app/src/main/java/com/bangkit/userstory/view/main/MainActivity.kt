package com.bangkit.userstory.view.main

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.userstory.R
import com.bangkit.userstory.ViewModelFactory
import com.bangkit.userstory.data.response.Story
import com.bangkit.userstory.databinding.ActivityMainBinding
import com.bangkit.userstory.view.authentication.welcome.WelcomeActivity
import com.bangkit.userstory.view.main.create.CreateActivity
import com.bangkit.userstory.view.resource.ListStoryAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                viewModel.getAllStories(user.token)
            }
        }

        viewModel.listStory.observe(this) {response ->
            if (response.listStory != null) {
                if (response.listStory.isEmpty()) {
                    binding.errorTextView.text = R.string.no_data.toString()
                    binding.errorTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.errorTextView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    setListStoryData(response.listStory)
                }
            } else {
                binding.errorTextView.text = response.message
                binding.errorTextView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle("Logout")
            setMessage("Are you sure you want to log out?")
            setPositiveButton("Yes") { _, _ ->
                viewModel.logout()
            }
            setNegativeButton("No", null)
        }.create()

        alertDialog.setOnShowListener {
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
            val dialogMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
            val layoutParams = alertDialog.window?.attributes
            layoutParams?.width = Resources.getSystem().displayMetrics.widthPixels - 2 * dialogMargin
            alertDialog.window?.attributes = layoutParams
        }

        alertDialog.show()
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

    private fun setListStoryData(stories: List<Story>) {
        val adapter = ListStoryAdapter<Story>()
        adapter.submitList(stories)
        binding.recyclerView.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}