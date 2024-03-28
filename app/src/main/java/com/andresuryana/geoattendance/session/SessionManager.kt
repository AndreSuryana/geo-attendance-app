package com.andresuryana.geoattendance.session

import java.util.Date

interface SessionManager {

    fun isLoggedOn(): Boolean

    fun insertSession(username: String, timestamp: Date)

    fun clearSession()
}