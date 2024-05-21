package com.bangkit.userstory.view.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bangkit.userstory.R
import com.bangkit.userstory.ViewModelFactory
import com.bangkit.userstory.data.request.LoginRequest
import com.bangkit.userstory.data.response.LoginResponse
import com.bangkit.userstory.databinding.ActivityLoginBinding
import com.bangkit.userstory.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val request = LoginRequest(email, password)

            viewModel.loginUser(request)

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

    private fun handleResponse(response: LoginResponse?) {
        response?.let {
            val alertDialog = AlertDialog.Builder(this).apply {
                if (it.error == false) {
                    setTitle(getString(R.string.success))
                    setMessage(getString(R.string.login_success))
                    setPositiveButton(getString(R.string.continue_)) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                } else if (it.error == true) {
                    setTitle(getString(R.string.failed))
                    setMessage(it.message)
                    setPositiveButton(getString(R.string.ok), null)
                }
            }.create()

            alertDialog.setOnShowListener {
                alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.navy))
                val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.navy))
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
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        val togetherTitle = AnimatorSet().apply {
            playTogether(title, message)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(emailTextView, emailEditTextLayout)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(passwordTextView, passwordEditTextLayout)
        }

        AnimatorSet().apply {
            playSequentially(
                togetherTitle,
                togetherEmail,
                togetherPassword,
                login)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

}