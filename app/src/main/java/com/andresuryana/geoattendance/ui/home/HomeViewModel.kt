package com.andresuryana.geoattendance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.geoattendance.data.repository.AttendanceRepository
import com.andresuryana.geoattendance.util.StringUtils.formatDate
import com.andresuryana.geoattendance.util.StringUtils.formatToTime12Hour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AttendanceRepository,
) : ViewModel() {

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    private val timer = Timer()

    init {
        setCurrentDate()
        startClock()
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

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}