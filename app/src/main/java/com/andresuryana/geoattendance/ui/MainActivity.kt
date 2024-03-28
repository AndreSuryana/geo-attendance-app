package com.andresuryana.geoattendance.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.databinding.ActivityMainBinding
import com.andresuryana.geoattendance.ui.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavigation()
        setupSplashScreen()
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHost.navController)
    }

    private fun setupSplashScreen() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady) {
                        // App ready
                        content.viewTreeObserver.removeOnPreDrawListener(this)

                        // Check should show onboarding first or no
                        if (viewModel.isShowOnboarding()) showOnBoarding()
                        true
                    } else {
                        // App isn't ready yet
                        false
                    }
                }
            }
        )
    }

    private fun showOnBoarding() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }
}