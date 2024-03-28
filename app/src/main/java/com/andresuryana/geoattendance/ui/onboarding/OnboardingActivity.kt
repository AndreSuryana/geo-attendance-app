package com.andresuryana.geoattendance.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andresuryana.geoattendance.databinding.ActivityOnboardingBinding
import com.andresuryana.geoattendance.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButton()
    }

    private fun setupButton() {
        binding.btnSubmit.setOnClickListener {
            val username = binding.etUsername.text.toString()
            viewModel.insertSession(username, this::onSessionInserted)
        }
    }

    private fun onSessionInserted(error: String?) {
        // Show error
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        } else {
            showMainScreen()
        }
    }

    private fun showMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}