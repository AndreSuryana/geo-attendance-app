package com.andresuryana.geoattendance.ui.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.data.model.AttendanceType
import com.andresuryana.geoattendance.data.model.AttendanceType.CHECK_IN
import com.andresuryana.geoattendance.data.model.AttendanceType.CHECK_OUT
import com.andresuryana.geoattendance.data.repository.AttendanceRepository
import com.andresuryana.geoattendance.master.MasterLocationManager
import com.andresuryana.geoattendance.session.SessionManager
import com.andresuryana.geoattendance.util.StringUtils.formatDate
import com.andresuryana.geoattendance.util.StringUtils.formatToTime12Hour
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AttendanceRepository,
    private val session: SessionManager,
    private val locationManager: MasterLocationManager,
) : ViewModel() {

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    private val timer = Timer()

    private val _actionType = MutableLiveData<AttendanceType>()
    val actionType: LiveData<AttendanceType> = _actionType

    private val _error = MutableSharedFlow<Int>()
    val error = _error.asSharedFlow()

    private var currentLocation: LatLng? = null
    private var currentDistance: Float = -1f

    init {
        setCurrentDate()
        startClock()

        getAttendanceState()
    }

    private fun setCurrentDate() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentDate.postValue(Date().formatDate())
        }
    }

    private fun startClock() {
        timer.scheduleAtFixedRate(timerTask {
            _currentTime.postValue(Calendar.getInstance().time.formatToTime12Hour())
        }, 0, 1000) // Updated every second
    }

    private fun getAttendanceState() {
        viewModelScope.launch(Dispatchers.IO) {
            val last = repository.getLastAttendance()
            if (last != null && last.type == CHECK_IN) {
                // State now: Check out
                _actionType.postValue(CHECK_OUT)
            } else {
                // State now: Check in
                _actionType.postValue(CHECK_IN)
            }
        }
    }

    fun isCheckIn(): Boolean = _actionType.value == CHECK_IN

    private suspend fun validateDistance(): Boolean {
        if (currentLocation != null) {
            currentLocation?.let {
                val attendanceLocation = Location("")
                attendanceLocation.latitude = it.latitude
                attendanceLocation.longitude = it.longitude

                val targetLocation = Location("")
                targetLocation.latitude = locationManager.getLatitude()
                targetLocation.longitude = locationManager.getLongitude()

                val distance = attendanceLocation.distanceTo(targetLocation)
                currentDistance = distance
                Log.d("HomeViewModel", "validateDistance: distance=$distance m")

                if (distance < DISTANCE_ALLOWED) {
                    Log.d("HomeViewModel", "validateDistance: Allowed!")
                    return true
                } else if (distance > DISTANCE_ALLOWED && distance < DISTANCE_ALMOST) {
                    Log.d("HomeViewModel", "validateDistance: Almost there!")
                    _error.emit(R.string.label_move_closer_1)
                    return false
                } else if (distance > DISTANCE_ALMOST && distance < DISTANCE_MOVE_CLOSER) {
                    Log.d("HomeViewModel", "validateDistance: Move closer!")
                    _error.emit(R.string.label_move_closer_2)
                    return false
                } else {
                    Log.d("HomeViewModel", "validateDistance: To far!")
                    _error.emit(R.string.label_move_closer_3)
                    return false
                }
            }
            return false
        } else {
            Log.d("HomeViewModel", "validateDistance: No GPS location!")
            return false
        }
    }

    fun checkIn() {
        viewModelScope.launch(Dispatchers.IO) {
            if (validateDistance()) {
                val username = session.getUsername()
                if (username != null) {
                    val attendance = Attendance(
                        type = CHECK_IN,
                        username = username,
                        distance = currentDistance,
                        timestamp = Date(System.currentTimeMillis())
                    )
                    repository.insertAttendance(attendance)

                    // Update state to: Check out
                    _actionType.postValue(CHECK_OUT)
                } else {
                    _error.emit(R.string.error_check_in)
                }
            }
        }
    }

    fun checkOut() {
        viewModelScope.launch(Dispatchers.IO) {
            if (validateDistance()) {
                val username = session.getUsername()
                if (username != null) {
                    val attendance = Attendance(
                        type = CHECK_OUT,
                        username = username,
                        distance = currentDistance,
                        timestamp = Date(System.currentTimeMillis())
                    )
                    repository.insertAttendance(attendance)

                    // Update state to: Check in
                    _actionType.postValue(CHECK_IN)
                } else {
                    _error.emit(R.string.error_check_out)
                }
            }
        }
    }

    fun setLocation(location: LatLng) {
        this.currentLocation = location
    }

    fun getUsername(): LiveData<String> = MutableLiveData(session.getUsername())

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    companion object {
        // In metres
        private const val DISTANCE_ALLOWED = 50
        private const val DISTANCE_ALMOST = 200
        private const val DISTANCE_MOVE_CLOSER = 500
    }
}