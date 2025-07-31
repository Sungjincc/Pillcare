package com.example.pillcare_capstone.adapter

import android.app.Dialog
import android.graphics.Color
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.MedicineTimePlus
import com.example.pillcare_capstone.databinding.ActivityDialogBinding
import com.example.pillcare_capstone.databinding.MedicineTimePlusListBinding

class MedicineTimePlusAdapter(
    private val timeList: MutableList<MedicineTimePlus>,
    private val inflater: LayoutInflater,
    private var isClickable: Boolean = true
) : RecyclerView.Adapter<MedicineTimePlusAdapter.TimeViewHolder>() {

    private var editingPosition = -1

    inner class TimeViewHolder(binding: MedicineTimePlusListBinding) : RecyclerView.ViewHolder(binding.root) {
        val medicineTimeText = binding.medicineTimeText
        val medicineDayText = binding.medicineDayText
        val editTimeButton = binding.editTimeButton
        val deleteTimeButton = binding.deleteTimeButton
        val cardView = binding.cardView // Add this line
        val days = listOf("월", "화", "수", "목", "금", "토", "일")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = MedicineTimePlusListBinding.inflate(inflater, parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val item = timeList[position]

        if (position == editingPosition) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.md_theme_light_surfaceVariant))
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
        }

        // 기본 시간 설정
        if (item.alarmTime.isBlank()) {
            item.alarmTime = "08:00"
        }

        // 시간 포맷 변경 (24시간 → 12시간 형식)
        val timeFormat = formatTime(item.alarmTime)
        holder.medicineTimeText.text = timeFormat

        // 선택된 요일들 표시
        val selectedDaysText = if (item.selectedDays.isEmpty()) {
            "요일을 선택하세요"
        } else {
            item.selectedDays.joinToString(", ")
        }
        holder.medicineDayText.text = selectedDaysText

        // 클릭 허용 상태에 따른 UI 설정
        if (isClickable) {
            holder.editTimeButton.visibility = View.VISIBLE
            holder.deleteTimeButton.visibility = View.VISIBLE

            // 시간 편집 버튼 클릭
            holder.editTimeButton.setOnClickListener {
                val currentPos = holder.adapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    editingPosition = currentPos
                    notifyItemChanged(currentPos)
                    showTimeAndDayPickerDialog(currentPos, holder)
                }
            }

            // 삭제 버튼 클릭
            holder.deleteTimeButton.setOnClickListener {
                if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                    timeList.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, timeList.size)
                }
            }
        } else {
            holder.editTimeButton.visibility = View.GONE
            holder.deleteTimeButton.visibility = View.GONE
        }
    }

    private fun formatTime(time: String): String {
        return try {
            val parts = time.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1]

            when {
                hour == 0 -> "오전 12:$minute"
                hour < 12 -> "오전 $hour:$minute"
                hour == 12 -> "오후 12:$minute"
                else -> "오후 ${hour - 12}:$minute"
            }
        } catch (e: Exception) {
            time // 파싱 실패 시 원본 반환
        }
    }

    private fun showTimeAndDayPickerDialog(position: Int, viewHolder: TimeViewHolder) {
        val context = viewHolder.itemView.context
        val dialog = Dialog(context)

        // 시간 선택을 위한 기본 다이얼로그 사용
        val binding = ActivityDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val hourPicker = binding.hourPicker
        val minutePicker = binding.minutePicker
        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        // 현재 시간으로 초기화
        val currentTime = timeList[position].alarmTime.split(":")
        hourPicker.value = currentTime.getOrNull(0)?.toIntOrNull() ?: 8
        minutePicker.value = currentTime.getOrNull(1)?.toIntOrNull() ?: 0

        hourPicker.setFormatter { i -> String.format("%02d", i) }
        minutePicker.setFormatter { i -> String.format("%02d", i) }

        val dayCheckBoxes = mutableMapOf<String, CheckBox>()

        // 요일 선택 체크박스들 (간단하게 구현)
        val dayContainerLayout = binding.dayContainerLayout
        viewHolder.days.forEach { day ->
            val dayLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                gravity = Gravity.CENTER_HORIZONTAL
            }

            val dayTextView = TextView(context).apply {
                text = day
                gravity = Gravity.CENTER
            }

            val checkBox = CheckBox(context).apply {
                isChecked = timeList[position].selectedDays.contains(day)
            }

            dayLayout.addView(dayTextView)
            dayLayout.addView(checkBox)
            dayCheckBoxes[day] = checkBox
            dayContainerLayout.addView(dayLayout)
        }

        binding.goToPreviousPageButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.successButton.setOnClickListener {
            val selectedHour = hourPicker.value.toString().padStart(2, '0')
            val selectedMinute = minutePicker.value.toString().padStart(2, '0')
            val selectedTime = "$selectedHour:$selectedMinute"

            // 선택된 요일들 수집
            val selectedDays = mutableListOf<String>()
            dayCheckBoxes.forEach { (day, checkBox) ->
                if (checkBox.isChecked) {
                    selectedDays.add(day)
                }
            }

            if (position != RecyclerView.NO_POSITION && position < timeList.size) {
                timeList[position].alarmTime = selectedTime
                timeList[position].selectedDays = selectedDays
                notifyItemChanged(position)
            }

            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            val previouslyEditingPosition = editingPosition
            editingPosition = -1
            if (previouslyEditingPosition != -1) {
                notifyItemChanged(previouslyEditingPosition)
            }
        }

        dialog.show()
    }

    fun setClickable(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = timeList.size
}