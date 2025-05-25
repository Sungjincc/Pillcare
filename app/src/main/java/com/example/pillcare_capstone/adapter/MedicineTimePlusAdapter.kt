package com.example.pillcare_capstone.adapter

import android.app.Dialog
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.MedicineTimePlus
import com.example.pillcare_capstone.databinding.ActivityDialogBinding
import com.example.pillcare_capstone.databinding.MedicineTimePlusListBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MedicineTimePlusAdapter(
    private val timeList: MutableList<MedicineTimePlus>,
    private val inflater: LayoutInflater,
    private var isClickable: Boolean = true
) : RecyclerView.Adapter<MedicineTimePlusAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(binding: MedicineTimePlusListBinding) : RecyclerView.ViewHolder(binding.root) {
        val recyclerViewSetMedicineTimeEfab: ExtendedFloatingActionButton = binding.recyclerViewSetMedicineTimeEfab
        val recyclerViewMedicineTimeDeleteEfab: ExtendedFloatingActionButton = binding.recyclerViewMedicineTimeDeleteEfab
        val recyclerViewDayContainer: LinearLayout = binding.recyclerViewdayContainer
        val days = listOf("월", "화", "수", "목", "금", "토", "일")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = MedicineTimePlusListBinding.inflate(inflater, parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val item = timeList[position]
        holder.recyclerViewDayContainer.removeAllViews()

        // 요일 버튼 렌더링
        holder.days.forEach { day ->
            val button = AppCompatButton(holder.itemView.context).apply {
                text = day
                textSize = 14f
                gravity = Gravity.CENTER
                setTextColor(if (day == "토" || day == "일") Color.RED else Color.WHITE)
                background = if (item.selectedDays.contains(day)) {
                    ContextCompat.getDrawable(context, R.drawable.bg_day_button_selected)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.bg_day_button_unselected)
                }
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setMargins(4, 0, 4, 0)
                }

                if (isClickable) {
                    setOnClickListener {
                        if (item.selectedDays.contains(day)) {
                            item.selectedDays.remove(day)
                            background = ContextCompat.getDrawable(context, R.drawable.bg_day_button_unselected)
                        } else {
                            item.selectedDays.add(day)
                            background = ContextCompat.getDrawable(context, R.drawable.bg_day_button_selected)
                        }
                    }
                }
            }
            holder.recyclerViewDayContainer.addView(button)
        }

        // 클릭 허용 상태
        if (isClickable) {
            holder.recyclerViewMedicineTimeDeleteEfab.visibility = View.VISIBLE
            holder.recyclerViewMedicineTimeDeleteEfab.setOnClickListener {
                if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                    timeList.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, timeList.size)
                }
            }

            holder.recyclerViewSetMedicineTimeEfab.setOnClickListener {
                val currentPos = holder.adapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    showTimePickerDialog(currentPos, holder)
                }
            }
        } else {
            holder.recyclerViewMedicineTimeDeleteEfab.visibility = View.GONE
            holder.recyclerViewSetMedicineTimeEfab.setOnClickListener(null)
            for (i in 0 until holder.recyclerViewDayContainer.childCount) {
                holder.recyclerViewDayContainer.getChildAt(i).isEnabled = false
            }
        }

        // 기존 저장된 시간 있으면 버튼에 표시
        if (item.alarmTime.isNotBlank()) {
            holder.recyclerViewSetMedicineTimeEfab.text = item.alarmTime
        }
    }

    private fun showTimePickerDialog(position: Int, viewHolder: TimeViewHolder) {
        val context = viewHolder.itemView.context
        val dialog = Dialog(context)
        val binding = ActivityDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val hourPicker = binding.hourPicker
        val minutePicker = binding.minutePicker
        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        binding.goToPreviousPageButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.successButton.setOnClickListener {
            val selectedHour = hourPicker.value.toString().padStart(2, '0')
            val selectedMinute = minutePicker.value.toString().padStart(2, '0')
            val selectedTime = "$selectedHour:$selectedMinute"

            if (position != RecyclerView.NO_POSITION && position < timeList.size) {
                timeList[position].alarmTime = selectedTime
                viewHolder.recyclerViewSetMedicineTimeEfab.text = selectedTime
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    fun setClickable(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = timeList.size
}