package com.andresuryana.geoattendance.session

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl(context: Context) : SessionManager {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun isLoggedOn(): Boolean = prefs.getString(KEY_USERNAME, null) != null

    override fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    override fun insertSession(username: String, timestamp: Date) {
        prefs.edit()
            .putString(KEY_USERNAME, username)
            .putLong(KEY_TIMESTAMP, timestamp.time)
            .apply()
    }

    override fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "session_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_TIMESTAMP = "timestamp"
    }
}