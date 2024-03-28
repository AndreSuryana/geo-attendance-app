package com.andresuryana.geoattendance.ui

import androidx.lifecycle.ViewModel
import com.andresuryana.geoattendance.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val session: SessionManager,
) : ViewModel() {

    var isReady = false

    init {
        // Set true when view model initialized
        isReady = true
    }

    fun isShowOnboarding(): Boolean {
        return !session.isLoggedOn()
    }
}