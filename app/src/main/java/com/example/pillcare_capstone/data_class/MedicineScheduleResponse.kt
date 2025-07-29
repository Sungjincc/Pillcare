package com.example.pillcare_capstone.data_class

data class MedicineScheduleResponse(
    val medicineName: String?,
    val schedules: List<ScheduleTime>,
    val pillCaseColor: String,
    val userId: Int
)
