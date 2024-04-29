package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.databinding.ActivityResultBinding
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_URI_IMAGE))
        val cancerName = intent.getStringExtra(EXTRA_CANCER_NAME)
        val confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, -1f)
        val percentageFormat = NumberFormat.getPercentInstance().apply {
            maximumFractionDigits = 2
        }

        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = "$cancerName ${percentageFormat.format(confidenceScore)}"
    }

    companion object {
        const val EXTRA_URI_IMAGE = "extra_uri_image"
        const val EXTRA_CANCER_NAME = "extra_cancer_name"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
    }
}
