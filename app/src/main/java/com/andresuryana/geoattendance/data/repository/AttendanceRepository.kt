package com.andresuryana.geoattendance.data.repository

import com.andresuryana.geoattendance.data.model.Attendance

interface AttendanceRepository {

    fun getAttendanceList(): List<Attendance>

    fun insertAttendance(attendance: Attendance)

    fun deleteAllAttendance()
}