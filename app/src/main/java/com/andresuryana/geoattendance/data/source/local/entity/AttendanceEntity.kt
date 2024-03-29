package com.andresuryana.geoattendance.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendances")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "distance") val distance: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)