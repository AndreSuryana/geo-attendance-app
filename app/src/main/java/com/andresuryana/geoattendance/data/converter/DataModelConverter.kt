package com.andresuryana.geoattendance.data.converter

import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.data.model.AttendanceType
import com.andresuryana.geoattendance.data.source.local.entity.AttendanceEntity
import java.util.Date

object DataModelConverter {

    fun AttendanceEntity.toAttendance(): Attendance {
        return Attendance(
            id = id,
            type = AttendanceType.valueOf(type),
            username = username,
            distance = distance.toFloat(),
            timestamp = Date(timestamp)
        )
    }

    fun Attendance.toEntity(): AttendanceEntity {
        return AttendanceEntity(
            id = id,
            type = type.name,
            username = username,
            distance = distance.toString(),
            timestamp = timestamp.time
        )
    }
}