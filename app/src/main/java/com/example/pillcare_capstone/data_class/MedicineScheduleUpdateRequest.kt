package com.example.pillcare_capstone.data_class

data class MedicineScheduleUpdateRequest(
    val userId: Int,
    val medicineName: String,
    val schedules: List<ScheduleTime>,
    val pillCaseColor: String
)
