package com.andresuryana.geoattendance.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: AttendanceRepository,
) : ViewModel() {

    private val _histories = MutableLiveData<List<Attendance>>()
    val histories: LiveData<List<Attendance>> = _histories

    init {
        getHistories()
    }

    private fun getHistories() {
        viewModelScope.launch(Dispatchers.IO) {
            _histories.postValue(repository.getAttendanceList())
        }
    }
}