package com.bangkit.userstory.view.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.userstory.R
import com.bangkit.userstory.ViewModelFactory
import com.bangkit.userstory.data.request.RegisterRequest
import com.bangkit.userstory.data.response.GeneralResponse
import com.bangkit.userstory.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val request = RegisterRequest(name, email, password)

            viewModel.registerUser(request)

            viewModel.user.observe(this) { response ->
                if (response.error != null) {
                    handleResponse(response)
                }
            }

            viewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }
        }
    }

    private fun handleResponse(response: GeneralResponse?) {
        response?.let {
            val alertDialog = AlertDialog.Builder(this).apply {
                if (it.error == false) {
                    setTitle("Success")
                    setMessage("Account ${binding.emailEditText.text} is succesfully created. Please login to continue")
                    setPositiveButton("Continue") { _, _ ->
                        finish()
                    }
                } else if (it.error == true) {
                    setTitle("Failed")
                    setMessage(it.message)
                    setPositiveButton("OK", null)
                }
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
        viewModel.clearResponse()
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
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(nameTextView, nameEditTextLayout)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(emailTextView, emailEditTextLayout)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(passwordTextView, passwordEditTextLayout)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                togetherName,
                togetherEmail,
                togetherPassword,
                signup)
            start()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}