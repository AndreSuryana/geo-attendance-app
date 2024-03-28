package com.andresuryana.geoattendance.master

import android.content.Context
import android.content.SharedPreferences

class MasterLocationManagerImpl(context: Context) : MasterLocationManager {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun setMasterLocation(latitude: Double, longitude: Double) {
        prefs.edit()
            .putLong(KEY_LATITUDE, latitude.toLong())
            .putLong(KEY_LONGITUDE, longitude.toLong())
            .apply()
    }

    override fun getLatitude(): Double = prefs.getLong(KEY_LATITUDE, DEFAULT_LATITUDE.toLong()).toDouble()

    override fun getLongitude(): Double = prefs.getLong(KEY_LONGITUDE, DEFAULT_LONGITUDE.toLong()).toDouble()

    companion object {
        private const val PREFS_NAME = "master_location_prefs"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        // Default location is set to Monumen Nasional
        private const val DEFAULT_LATITUDE = -6.175400
        private const val DEFAULT_LONGITUDE = 106.827200
    }
}