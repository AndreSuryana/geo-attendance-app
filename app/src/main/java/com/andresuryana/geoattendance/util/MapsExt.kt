package com.andresuryana.geoattendance.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap

object MapsExt {

    fun GoogleMap.enableMyLocation(
        activity: Activity,
        permissionLauncher: ActivityResultLauncher<String>,
    ) {
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            this.isMyLocationEnabled = true
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}