package com.andresuryana.geoattendance.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.geoattendance.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val session: SessionManager,
) : ViewModel() {

    fun insertSession(username: String, callback: (error: String?) -> Unit) {
        viewModelScope.launch {
            // Input validation
            if (username.isEmpty()) {
                callback.invoke("Please fill username")
                return@launch
            }

            // Insert session
            session.insertSession(username, Date(System.currentTimeMillis()))
            callback.invoke(null)
        }
    }
}