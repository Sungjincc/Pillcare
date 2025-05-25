package com.example.pillcare_capstone.utils

import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.data_class.MedicineScheduleRequest

fun convertKoreanDaysToEnglish(koreanDays: List<String>): List<String> {
    val dayMap = mapOf(
        "월" to "Mon", "화" to "Tue", "수" to "Wed",
        "목" to "Thu", "금" to "Fri", "토" to "Sat", "일" to "Sun"
    )
    return koreanDays.mapNotNull { dayMap[it] }
}

fun MedicinePlus.toRequest(userId: Int): MedicineScheduleRequest {
    val allDays = this.selectedDays.toMutableSet()
    this.timeList.forEach {
        allDays.addAll(it.selectedDays)
    }

    val allTimes = this.timeList.map { it.alarmTime }

    return MedicineScheduleRequest(
        userId = userId,
        medicineName = this.medicineName,
        daysOfWeek = convertKoreanDaysToEnglish(allDays.toList()),
        times = allTimes
    )
}