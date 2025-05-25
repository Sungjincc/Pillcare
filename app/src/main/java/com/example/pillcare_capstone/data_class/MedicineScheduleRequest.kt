package com.example.pillcare_capstone.data_class

data class ScheduleTime(
    val time: String,
    val daysOfWeek: List<String>
)

data class MedicineScheduleRequest(
    val userId: Int,
    val medicineName: String,
    val schedules: List<ScheduleTime>
)