package com.andresuryana.geoattendance.di

import android.content.Context
import androidx.room.Room
import com.andresuryana.geoattendance.data.repository.AttendanceRepository
import com.andresuryana.geoattendance.data.repository.AttendanceRepositoryImpl
import com.andresuryana.geoattendance.data.source.local.database.AttendanceDatabase
import com.andresuryana.geoattendance.session.SessionManager
import com.andresuryana.geoattendance.session.SessionManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AttendanceModule {

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager =
        SessionManagerImpl(context)

    @Provides
    @Singleton
    fun provideAttendanceDatabase(@ApplicationContext context: Context): AttendanceDatabase =
        Room.databaseBuilder(context, AttendanceDatabase::class.java, "geo-attendance-db")
            .build()

    @Provides
    @Singleton
    fun provideAttendanceRepository(local: AttendanceDatabase): AttendanceRepository =
        AttendanceRepositoryImpl(local)
}