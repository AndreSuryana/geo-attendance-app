package com.andresuryana.geoattendance.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryViewHolder>() {

    private val list: ArrayList<Attendance> = ArrayList()

    fun setList(list: List<Attendance>) {
        val diffCallback = HistoryDiffCallback(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.list.clear()
        this.list.addAll(list)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class HistoryDiffCallback(
        private val oldList: List<Attendance>,
        private val newList: List<Attendance>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}