package com.andresuryana.geoattendance.master

import android.content.Context
import android.content.SharedPreferences

class MasterLocationManagerImpl(context: Context) : MasterLocationManager {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun setMasterLocation(latitude: Double, longitude: Double) {
        prefs.edit()
            .putString(KEY_LATITUDE, latitude.toString())
            .putString(KEY_LONGITUDE, longitude.toString())
            .apply()
    }

    override fun getLatitude(): Double {
        val latitudeStr = prefs.getString(KEY_LATITUDE, DEFAULT_LATITUDE.toString())
        return latitudeStr?.toDoubleOrNull() ?: DEFAULT_LATITUDE
    }

    override fun getLongitude(): Double {
        val longitudeStr = prefs.getString(KEY_LONGITUDE, DEFAULT_LONGITUDE.toString())
        return longitudeStr?.toDoubleOrNull() ?: DEFAULT_LONGITUDE
    }

    companion object {
        private const val PREFS_NAME = "master_location_prefs"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        // Default location is set to Monumen Nasional
        private const val DEFAULT_LATITUDE = -6.175400
        private const val DEFAULT_LONGITUDE = 106.827200
    }
}