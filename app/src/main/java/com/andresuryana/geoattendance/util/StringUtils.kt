package com.andresuryana.geoattendance.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StringUtils {

    private const val FORMAT_DATE = "E, dd MMM yyyy"
    private const val FORMAT_TIME = "h:mm a"

    fun Date.formatToDayMonthYear(): String {
        val dateFormat = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
        return dateFormat.format(this)
    }

    fun Date.formatToTime12Hour(): String {
        val timeFormat = SimpleDateFormat(FORMAT_TIME, Locale.getDefault())
        return timeFormat.format(this)
    }
}