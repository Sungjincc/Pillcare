package com.example.pillcare_capstone.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.data_class.MedicineScheduleUpdateRequest
import com.example.pillcare_capstone.data_class.MedicineTimePlus
import com.example.pillcare_capstone.data_class.ScheduleTime
import com.example.pillcare_capstone.databinding.ActivityDialogBinding
import com.example.pillcare_capstone.databinding.MedicinePlusListBinding
import com.example.pillcare_capstone.network.RetrofitClient
import com.example.pillcare_capstone.utils.convertKoreanDaysToEnglish
import com.example.pillcare_capstone.utils.toRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MedicinePlusAdapter(
    var medicinePlusList: MutableList<MedicinePlus>,
    private var inflater: LayoutInflater,
    private val userId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder(val binding: MedicinePlusListBinding) : RecyclerView.ViewHolder(binding.root) {
        val medicineNameEditText = binding.medicineNameEditText
        val originalMedicineNameEditTextBackground = binding.medicineNameEditText.background
        val medicineNameText = binding.medicineNameText
        val dayContainer = binding.dayContainer
        val setMedicineTimeEfab = binding.setMedicineTimeEfab
        val medicineTimePlusRecyclerView = binding.medicineTimePlusRecyclerView
        val medicineTimePlusFab = binding.medicineTimePlusFab
        val medicineModifyEfab = binding.medicineModifyEfab
        val medicineDeleteEfab = binding.medicineDeleteEfab
        val medicinePlusSuccessButton = binding.medicinePlusSuccessButton
        var isEditMode: Boolean = false
        val days = listOf("월", "화", "수", "목", "금", "토", "일")

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
            }
        }
        // 수정 불가 기능
        fun setDisableEditMode() {
            medicineNameEditText.isEnabled = false
            medicineNameText.text = medicineNameEditText.text.toString()
            medicineNameText.visibility = View.VISIBLE
            medicineNameEditText.visibility = View.GONE
            medicineNameEditText.setBackgroundColor(Color.TRANSPARENT)
            medicineModifyEfab.visibility = View.VISIBLE
            medicineDeleteEfab.visibility = View.VISIBLE
            medicinePlusSuccessButton.visibility = View.GONE
            medicineTimePlusFab.visibility = View.GONE
            setMedicineTimeEfab.isClickable = false
            for (i in 0 until dayContainer.childCount) {
                dayContainer.getChildAt(i).isEnabled = false
            }
            isEditMode = false
        }

        // 수정 가능 기능
        fun setEnableEditMode(originalBackground: Drawable?) {
            medicineNameEditText.isEnabled = true
            medicineNameText.visibility = View.GONE
            medicineNameEditText.visibility = View.VISIBLE
            medicineModifyEfab.visibility = View.GONE
            medicineDeleteEfab.visibility = View.GONE
            medicinePlusSuccessButton.visibility = View.VISIBLE
            medicineTimePlusFab.visibility = View.VISIBLE
            setMedicineTimeEfab.isClickable = true
            setMedicineTimeEfab.isEnabled = true
            medicineNameEditText.background = originalBackground
            for (i in 0 until dayContainer.childCount) {
                dayContainer.getChildAt(i).isEnabled = true
            }
            isEditMode = true

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MedicinePlusListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val item = medicinePlusList[position]
        viewHolder.medicineNameEditText.setText(item.medicineName)
        viewHolder.medicineNameText.text = item.medicineName
        val defaultTime = "12:00"
        val alarmTime = item.alarmTime?.takeIf { it.isNotBlank() } ?: defaultTime
        viewHolder.setMedicineTimeEfab.text = alarmTime
        item.alarmTime = alarmTime
        viewHolder.dayContainer.removeAllViews()

        viewHolder.days.forEach { day ->
            val dayButton = AppCompatButton(viewHolder.itemView.context).apply {
                text = day
                gravity = Gravity.CENTER
                textSize = 16f
                setTextColor(if (day == "일" || day == "토") Color.RED else Color.WHITE)
                setPadding(20, 20, 20, 20)
                isSelected = item.selectedDays.contains(day)
                background = if (isSelected) {
                    ContextCompat.getDrawable(context, R.drawable.bg_day_button_selected)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.bg_day_button_unselected)
                }
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setMargins(8, 0, 8, 0)
                }

                setOnClickListener {
                    if (item.selectedDays.contains(day)) {
                        item.selectedDays.remove(day)
                        isSelected = false
                        background = ContextCompat.getDrawable(context, R.drawable.bg_day_button_unselected)
                    } else {
                        item.selectedDays.add(day)
                        isSelected = true
                        background = ContextCompat.getDrawable(context, R.drawable.bg_day_button_selected)
                    }
                }
            }
            viewHolder.dayContainer.addView(dayButton)
            viewHolder.setDisableEditMode()
        }

        val timeAdapter = MedicineTimePlusAdapter(item.timeList, inflater, true)
        viewHolder.medicineTimePlusRecyclerView.adapter = timeAdapter
        viewHolder.medicineTimePlusRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        viewHolder.medicineTimePlusRecyclerView.isNestedScrollingEnabled = false

        // 약 시간 추가 버튼 클릭
        viewHolder.medicineTimePlusFab.setOnClickListener {
            item.timeList.add(MedicineTimePlus())
            timeAdapter.notifyItemInserted(item.timeList.size - 1)
        }

        //약 복용 시간 버튼 클릭
        viewHolder.setMedicineTimeEfab.setOnClickListener {
            if (!viewHolder.isEditMode) return@setOnClickListener
            val context = viewHolder.itemView.context
            val dialog = Dialog(context)

            // ViewBinding 사용
            val binding = ActivityDialogBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)

            // NumberPicker 값 설정
            val hourPicker = binding.hourPicker
            val minutePicker = binding.minutePicker
            hourPicker.minValue = 0
            hourPicker.maxValue = 23
            minutePicker.minValue = 0
            minutePicker.maxValue = 59

            hourPicker.value = 12
            minutePicker.value = 0

            hourPicker.setFormatter { i -> String.format("%02d", i) }
            minutePicker.setFormatter { i -> String.format("%02d", i) }

            // 이전 버튼 클릭시 다이어로그 취소
            binding.goToPreviousPageButton.setOnClickListener {
                dialog.dismiss()
            }

            // 저장 버튼 클릭 시 시간 반영
            binding.successButton.setOnClickListener {
                val selectedHour = hourPicker.value.toString().padStart(2, '0')
                val selectedMinute = minutePicker.value.toString().padStart(2, '0')
                viewHolder.setMedicineTimeEfab.text = "$selectedHour:$selectedMinute"


                dialog.dismiss()
            }

            dialog.show()
        }

        // 약 추가 완료 버튼 클릭
        viewHolder.medicinePlusSuccessButton.setOnClickListener {
            item.medicineName = viewHolder.medicineNameEditText.text.toString()
            item.alarmTime = viewHolder.setMedicineTimeEfab.text.toString()
            viewHolder.setDisableEditMode()
            timeAdapter.setClickable(false)

            val schedules = mutableListOf<ScheduleTime>()
            val topTime = item.alarmTime
            val topDays = item.selectedDays
            if (topTime.isNotBlank() && topDays.isNotEmpty()) {
                schedules.add(
                    ScheduleTime(
                        time = topTime,
                        daysOfWeek = convertKoreanDaysToEnglish(topDays)
                    )
                )
            }

            schedules.addAll(item.timeList.map {
                ScheduleTime(
                    time = it.alarmTime,
                    daysOfWeek = convertKoreanDaysToEnglish(it.selectedDays)
                )
            })

            val updateRequest = MedicineScheduleUpdateRequest(
                userId = userId,
                medicineName = item.medicineName,
                pillCaseColor = item.pillCaseColor?.name?.lowercase() ?: "",
                schedules = schedules
            )


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.apiService.updateSchedule(updateRequest)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(viewHolder.itemView.context, "저장 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("업데이트 실패", "${response.code()}: $errorBody")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("업데이트 예외", e.localizedMessage ?: "예외 발생")
                    }
                }
            }
        }

        //약 수정 버튼 클릭
        var isEditing = false
        viewHolder.medicineModifyEfab.setOnClickListener {
            if (!isEditing) {
                // [1] 수정 시작
                isEditing = true
                viewHolder.setEnableEditMode(viewHolder.originalMedicineNameEditTextBackground)
                timeAdapter.setClickable(true)

            } else {
                // [2] 수정 종료 (UI 반영만, 서버 전송 없음)
                isEditing = false
                viewHolder.setDisableEditMode()
                timeAdapter.setClickable(false)

                // 입력값은 로컬에만 반영 (서버 전송 제거됨)
                item.medicineName = viewHolder.medicineNameEditText.text.toString()
                item.alarmTime = viewHolder.setMedicineTimeEfab.text.toString()
            }
        }

        //약 삭제 버튼 클릭

        viewHolder.medicineDeleteEfab.setOnClickListener {
            val context = holder.itemView.context
            val cancelDialog = AlertDialog.Builder(context)
                .setTitle("삭제 확인")
                .setMessage("정말 이 약 정보를 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, _ ->

                    // 💬 서버에 삭제 요청
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val pillColor = item.pillCaseColor?.name?.lowercase() ?: ""
                            val response =
                                RetrofitClient.apiService.deleteSchedule(userId, pillColor)
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
                                    removeItem(holder.adapterPosition)
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.e("삭제 실패", "${response.code()}: $errorBody")
                                    Toast.makeText(
                                        context,
                                        "서버 오류: ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Log.e("삭제 예외", e.localizedMessage ?: "알 수 없는 오류")
                                Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            cancelDialog.setOnShowListener {
                cancelDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    ?.setTextColor(ContextCompat.getColor(context, R.color.textMainColor))
                cancelDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(ContextCompat.getColor(context, R.color.gray))
            }

            cancelDialog.show()
        }
    }
    override fun getItemCount(): Int {
        return medicinePlusList.size
    }

    // 리스트 아이템 삭제 기능
    fun removeItem(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            medicinePlusList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}