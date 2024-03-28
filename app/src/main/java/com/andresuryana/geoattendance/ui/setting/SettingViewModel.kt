package com.andresuryana.geoattendance.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.geoattendance.master.MasterLocationManager
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val locationManager: MasterLocationManager,
) : ViewModel() {

    private val _location = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location

    private var currentLocation: LatLng? = null

    init {
        getMasterLocation()
    }

    private fun getMasterLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val latitude = locationManager.getLatitude()
            val longitude = locationManager.getLongitude()
            _location.postValue(LatLng(latitude, longitude))
        }
    }

    fun updateMasterLocation(onDone: (error: String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check stored/selected location
            if (currentLocation == null) {
                onDone.invoke("Failed to update master location!")
                return@launch
            }

            // Update master location
            currentLocation?.let {
                locationManager.setMasterLocation(it.latitude, it.longitude)
                onDone.invoke(null)
            }
        }
    }

    fun setCurrentLocation(location: LatLng) {
        currentLocation = location
    }
}