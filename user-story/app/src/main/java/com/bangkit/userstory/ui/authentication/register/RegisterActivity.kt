package com.bangkit.userstory.ui.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
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
import com.bangkit.userstory.data.remote.request.RegisterRequest
import com.bangkit.userstory.data.remote.response.GeneralResponse
import com.bangkit.userstory.databinding.ActivityRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            hideKeyboard()

            if (binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null) {
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

        binding.passwordEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.signupButton.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun handleResponse(response: GeneralResponse?) {
        response?.let {
            if (it.error == false) {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.success))
                    .setMessage(getString(R.string.register_success))
                    .setPositiveButton(getString(R.string.continue_)) { _, _ ->
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