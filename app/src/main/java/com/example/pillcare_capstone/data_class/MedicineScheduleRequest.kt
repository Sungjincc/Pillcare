package com.example.pillcare_capstone.data_class

class MedicineScheduleRequest (
    val userId: Int,
    val medicineName: String,
    val daysOfWeek: List<String>,  // 영어 요일로 맞춰야 함
    val times: List<String>
)