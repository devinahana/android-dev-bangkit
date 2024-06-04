package com.bangkit.userstory.ui.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bangkit.userstory.R
import com.bangkit.userstory.ViewModelFactory
import com.bangkit.userstory.data.remote.request.LoginRequest
import com.bangkit.userstory.data.remote.response.LoginResponse
import com.bangkit.userstory.databinding.ActivityLoginBinding
import com.bangkit.userstory.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            hideKeyboard()
            if (binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null) {
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

        binding.passwordEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.loginButton.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun handleResponse(response: LoginResponse?) {
        response?.let {
            if (it.error == false) {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.success))
                    .setMessage(getString(R.string.login_success))
                    .setPositiveButton(getString(R.string.continue_)) { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .show()
            } else if (it.error == true) {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.failed))
                    .setMessage(it.message)
                    .setPositiveButton(getString(R.string.ok), null)
                    .show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            }
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

    private fun hideKeyboard() {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = this.currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

}