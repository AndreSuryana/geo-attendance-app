package com.andresuryana.geoattendance.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andresuryana.geoattendance.data.source.local.dao.AttendanceDao
import com.andresuryana.geoattendance.data.source.local.entity.AttendanceEntity

@Database(
    entities = [AttendanceEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AttendanceDatabase : RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao
}