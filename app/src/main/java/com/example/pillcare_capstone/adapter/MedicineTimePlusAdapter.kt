package com.example.pillcare_capstone.adapter

import android.app.Dialog
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.MedicineTimePlusTime
import com.example.pillcare_capstone.databinding.ActivityDialogBinding
import com.example.pillcare_capstone.databinding.MedicineTimePlusListBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MedicineTimePlusAdapter(
    private val timeList: MutableList<MedicineTimePlusTime>,
    private val inflater: LayoutInflater,
    private var isClickable : Boolean =true
) : RecyclerView.Adapter<MedicineTimePlusAdapter.TimeViewHolder>() {

    inner class TimeViewHolder( binding: MedicineTimePlusListBinding) : RecyclerView.ViewHolder(binding.root) {
        val recyclerViewSetMedicineTimeEfab: ExtendedFloatingActionButton = binding.recyclerViewSetMedicineTimeEfab
        val recyclerViewMedicineTimeDeleteEfab : ExtendedFloatingActionButton = binding.recyclerViewMedicineTimeDeleteEfab
        val recyclerViewdayContainer: LinearLayout = binding.recyclerViewdayContainer
        val days = listOf("월", "화", "수", "목", "금", "토", "일")

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val guardianMemo = timeList[position]
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = MedicineTimePlusListBinding.inflate(inflater, parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val item = timeList[position]
        holder.recyclerViewdayContainer.removeAllViews()

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
                            background = ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_day_button_unselected
                            )
                        } else {
                            item.selectedDays.add(day)
                            background = ContextCompat.getDrawable(
                                context,
                                R.drawable.bg_day_button_selected
                            )
                        }
                    }
                }
            }

            holder.recyclerViewdayContainer.addView(button)
        }
        if(isClickable)
        {
            holder.recyclerViewMedicineTimeDeleteEfab.visibility = View.VISIBLE
            holder.recyclerViewMedicineTimeDeleteEfab.setOnClickListener {
                timeList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                notifyItemRangeChanged(holder.adapterPosition, timeList.size)
            }
            holder.recyclerViewSetMedicineTimeEfab.setOnClickListener {
                showTimePickerDialog(holder)
            }

        }else
        {
            holder.recyclerViewMedicineTimeDeleteEfab.visibility = View.GONE
            holder.recyclerViewSetMedicineTimeEfab.setOnClickListener(null)
            // 요일 버튼 비활성화
            for (i in 0 until holder.recyclerViewdayContainer.childCount) {
                holder.recyclerViewdayContainer.getChildAt(i).isEnabled = false
            }
        }
    }
    // 커스텀 다이얼로그 생성
    private fun showTimePickerDialog(viewHolder: TimeViewHolder) {
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

        // 이전 버튼 클릭시 다이어로그 취소
        binding.goToPreviousPageButton.setOnClickListener {
            dialog.dismiss()
        }

        // 저장 버튼 클릭 시 시간 반영
        binding.successButton.setOnClickListener {
            val selectedHour = hourPicker.value.toString().padStart(2, '0')
            val selectedMinute = minutePicker.value.toString().padStart(2, '0')
            viewHolder.recyclerViewSetMedicineTimeEfab.text = "$selectedHour:$selectedMinute"
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