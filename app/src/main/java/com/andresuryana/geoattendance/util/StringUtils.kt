package com.andresuryana.geoattendance.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StringUtils {

    private const val FORMAT_DATE_DEFAULT = "dd MMM yyyy"
    const val FORMAT_DATE_WITH_DAY = "E, dd MMM yyyy"
    private const val FORMAT_TIME = "h:mm a"

    fun Date.formatDate(pattern: String = FORMAT_DATE_DEFAULT): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(this)
    }

    fun Date.formatToTime12Hour(): String {
        val timeFormat = SimpleDateFormat(FORMAT_TIME, Locale.getDefault())
        return timeFormat.format(this)
    }
}