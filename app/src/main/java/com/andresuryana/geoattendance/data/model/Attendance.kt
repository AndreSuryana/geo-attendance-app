package com.andresuryana.geoattendance.data.model

import java.util.Date

data class Attendance(
    val id: Int? = null,
    val type: AttendanceType,
    val username: String,
    val distance: Float,
    val timestamp: Date
)