package com.andresuryana.geoattendance.ui.history

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.data.model.AttendanceType
import com.andresuryana.geoattendance.databinding.ItemHistoryBinding
import com.andresuryana.geoattendance.util.StringUtils.FORMAT_DATE_WITH_DAY
import com.andresuryana.geoattendance.util.StringUtils.formatDate
import com.andresuryana.geoattendance.util.StringUtils.formatToTime12Hour

class HistoryViewHolder(private val binding: ItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Attendance) {
        if (item.type == AttendanceType.CHECK_IN) {
            binding.tvTitle.setText(R.string.title_checked_in)
            binding.ivIcon.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(binding.root.context, R.color.green)
            )
            binding.ivIcon.rotation = 180f
        } else {
            binding.tvTitle.setText(R.string.title_checked_out)
            binding.ivIcon.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(binding.root.context, R.color.red)
            )
        }
        binding.tvDate.text = item.timestamp.formatDate(FORMAT_DATE_WITH_DAY)
        binding.tvTime.text = item.timestamp.formatToTime12Hour()

    }
}