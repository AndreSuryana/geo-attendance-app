package com.andresuryana.geoattendance.data.model

import java.util.Date

data class Attendance(
    val id: Int,
    val type: AttendanceType,
    val username: String,
    val timestamp: Date
)