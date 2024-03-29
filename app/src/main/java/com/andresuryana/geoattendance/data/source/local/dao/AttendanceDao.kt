package com.andresuryana.geoattendance.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.andresuryana.geoattendance.data.source.local.entity.AttendanceEntity

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM attendances ORDER BY timestamp DESC")
    fun getAttendanceList(): List<AttendanceEntity>

    @Query("SELECT * FROM attendances ORDER BY timestamp DESC")
    fun getLastAttendance(): AttendanceEntity?

    @Insert
    fun insertAttendance(attendance: AttendanceEntity)

    @Query("DELETE FROM attendances")
    fun deleteAll()
}