package com.andresuryana.geoattendance.data.repository

import com.andresuryana.geoattendance.data.converter.DataModelConverter.toAttendance
import com.andresuryana.geoattendance.data.converter.DataModelConverter.toEntity
import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.data.source.local.database.AttendanceDatabase
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val local: AttendanceDatabase,
) : AttendanceRepository {

    override fun getAttendanceList(): List<Attendance> =
        local.attendanceDao().getAttendanceList().map { it.toAttendance() }

    override fun getLastAttendance(): Attendance? =
        local.attendanceDao().getLastAttendance()?.toAttendance()

    override fun insertAttendance(attendance: Attendance) {
        local.attendanceDao().insertAttendance(attendance.toEntity())
    }

    override fun deleteAllAttendance() {
        local.attendanceDao().deleteAll()
    }
}