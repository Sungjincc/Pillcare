package com.example.pillcare_capstone.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.data_class.MedicineScheduleUpdateRequest
import com.example.pillcare_capstone.data_class.MedicineTimePlus
import com.example.pillcare_capstone.data_class.PillCaseColor
import com.example.pillcare_capstone.data_class.ScheduleTime
import com.example.pillcare_capstone.databinding.MedicinePlusListBinding
import com.example.pillcare_capstone.network.RetrofitClient
import com.example.pillcare_capstone.utils.convertKoreanDaysToEnglish
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
        val medicineNameInputLayout = binding.medicineNameInputLayout
        val medicineImage = binding.medicineImage
        val medicineNameText = binding.medicineNameText
        val setMedicineTimeEfab = binding.setMedicineTimeEfab
        val medicineTimeRecyclerView = binding.medicineTimeRecyclerView
        val editMedicineButton = binding.editMedicineButton
        val deleteMedicineButton = binding.deleteMedicineButton
        val saveMedicineButton = binding.saveMedicineButton
        val cardView = binding.cardView
        var isEditMode: Boolean = false
        val days = listOf("월", "화", "수", "목", "금", "토", "일")

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
            }
        }
        
        // 수정 불가 기능
        fun setDisableEditMode() {
            medicineNameInputLayout.visibility = View.GONE
            medicineNameText.visibility = View.VISIBLE
            medicineNameText.text = medicineNameEditText.text.toString()
            
            editMedicineButton.visibility = View.VISIBLE
            deleteMedicineButton.visibility = View.VISIBLE
            saveMedicineButton.visibility = View.GONE
            
            setMedicineTimeEfab.visibility = View.GONE
            setMedicineTimeEfab.isClickable = false
            medicineTimeRecyclerView.visibility = View.GONE
            isEditMode = false
            cardView.setCardBackgroundColor(Color.WHITE)
        }

        // 수정 가능 기능
        fun setEnableEditMode() {
            medicineNameInputLayout.visibility = View.VISIBLE
            medicineNameText.visibility = View.GONE
            
            editMedicineButton.visibility = View.GONE
            deleteMedicineButton.visibility = View.GONE
            saveMedicineButton.visibility = View.VISIBLE
            
            setMedicineTimeEfab.visibility = View.VISIBLE
            setMedicineTimeEfab.isClickable = true
            setMedicineTimeEfab.isEnabled = true
            medicineTimeRecyclerView.visibility = View.VISIBLE
            isEditMode = true
            cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.md_theme_light_surface))
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
        
        // 약물 이미지 설정
        val pillImageRes = when (item.pillCaseColor) {
            PillCaseColor.RED -> R.drawable.bg_pill_red
            PillCaseColor.YELLOW -> R.drawable.bg_pill_yellow
            PillCaseColor.GREEN -> R.drawable.bg_pill_green
            null -> R.drawable.bg_pill_red // 기본값
        }
        viewHolder.medicineImage.setImageResource(pillImageRes)
        
        // 색상 인디케이터 설정
        val colorIndicator = when (item.pillCaseColor) {
            PillCaseColor.RED -> R.color.pill_red
            PillCaseColor.YELLOW -> R.color.pill_yellow
            PillCaseColor.GREEN -> R.color.pill_green
            null -> R.color.pill_red // 기본값
        }
        viewHolder.binding.medicineColorIndicator.setBackgroundColor(
            ContextCompat.getColor(viewHolder.itemView.context, colorIndicator)
        )
        
        val defaultTime = "12:00"
        val alarmTime = item.alarmTime?.takeIf { it.isNotBlank() } ?: defaultTime
        viewHolder.setMedicineTimeEfab.text = "시간 추가"
        item.alarmTime = alarmTime

        // 약물 시간 리스트 설정
        val timeAdapter = MedicineTimePlusAdapter(item.timeList, inflater, true)
        viewHolder.medicineTimeRecyclerView.adapter = timeAdapter
        viewHolder.medicineTimeRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        viewHolder.medicineTimeRecyclerView.isNestedScrollingEnabled = false

        // 초기 상태 설정
        if (item.isNew) {
            viewHolder.setEnableEditMode()
            item.isNew = false
        } else {
            viewHolder.setDisableEditMode()
        }
        viewHolder.medicineTimeRecyclerView.visibility = if (viewHolder.isEditMode) View.VISIBLE else View.GONE


        //약 복용 시간 버튼 클릭
        viewHolder.setMedicineTimeEfab.setOnClickListener {
            if (!viewHolder.isEditMode) return@setOnClickListener
            
            // 새로운 시간 추가
            item.timeList.add(MedicineTimePlus())
            timeAdapter.notifyItemInserted(item.timeList.size - 1)
        }

        // 약 저장 버튼 클릭
        viewHolder.saveMedicineButton.setOnClickListener {
            item.medicineName = viewHolder.medicineNameEditText.text.toString()
            
            if (item.medicineName.isBlank()) {
                Toast.makeText(viewHolder.itemView.context, "약물 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            viewHolder.setDisableEditMode()
            timeAdapter.setClickable(false)

            val schedules = mutableListOf<ScheduleTime>()

            // 추가된 시간들 처리
            schedules.addAll(item.timeList.filter { 
                it.alarmTime.isNotBlank() && it.selectedDays.isNotEmpty() 
            }.map {
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
                    Log.d("업데이트 JSON", com.google.gson.Gson().toJson(updateRequest))
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Log.d("업데이트 JSON", com.google.gson.Gson().toJson(updateRequest))
                            Toast.makeText(viewHolder.itemView.context, "저장 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (!errorBody.isNullOrBlank()) {
                                Log.e("업데이트 실패", "${response.code()}: ${errorBody.take(300)}")
                            } else {
                                Log.e("업데이트 실패", "${response.code()}: 응답 내용 없음")
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("업데이트 예외", e.localizedMessage ?: "예외 발생")
                        Toast.makeText(viewHolder.itemView.context, "에러: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //약 수정 버튼 클릭
        viewHolder.editMedicineButton.setOnClickListener {
            viewHolder.setEnableEditMode()
            timeAdapter.setClickable(true)
        }

        //약 삭제 버튼 클릭
        viewHolder.deleteMedicineButton.setOnClickListener {
            val context = holder.itemView.context
            val cancelDialog = AlertDialog.Builder(context)
                .setTitle("삭제 확인")
                .setMessage("정말 이 약 정보를 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, _ ->

                    // 서버에 삭제 요청
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
                    ?.setTextColor(ContextCompat.getColor(context, R.color.md_theme_light_error))
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