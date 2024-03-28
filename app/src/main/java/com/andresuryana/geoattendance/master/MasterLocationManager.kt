package com.andresuryana.geoattendance.master

interface MasterLocationManager {
    fun setMasterLocation(latitude: Double, longitude: Double)
    fun getLatitude(): Double
    fun getLongitude(): Double
}