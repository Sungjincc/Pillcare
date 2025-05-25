package com.example.pillcare_capstone.utils

import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.data_class.MedicineScheduleRequest
import com.example.pillcare_capstone.data_class.ScheduleTime

fun convertKoreanDaysToEnglish(koreanDays: List<String>): List<String> {
    val dayMap = mapOf(
        "월" to "Mon", "화" to "Tue", "수" to "Wed",
        "목" to "Thu", "금" to "Fri", "토" to "Sat", "일" to "Sun"
    )
    return koreanDays.mapNotNull { dayMap[it] }
}

fun MedicinePlus.toRequest(userId: Int): MedicineScheduleRequest {
    val schedules = mutableListOf<ScheduleTime>()

    // 맨 위 alarmTime + selectedDays (통합 사용 시)
    if (this.alarmTime.isNotBlank() && this.selectedDays.isNotEmpty()) {
        schedules.add(
            ScheduleTime(
                time = this.alarmTime,
                daysOfWeek = convertKoreanDaysToEnglish(this.selectedDays)
            )
        )
    }

    // 리스트 기반 시간들
    this.timeList.forEach { timeItem ->
        if (timeItem.alarmTime.isNotBlank()) {
            schedules.add(
                ScheduleTime(
                    time = timeItem.alarmTime,
                    daysOfWeek = convertKoreanDaysToEnglish(timeItem.selectedDays)
                )
            )
        }
    }

    return MedicineScheduleRequest(
        userId = userId,
        medicineName = this.medicineName,
        schedules = schedules,
        pillCaseColor = this.pillCaseColor?.name?.lowercase() ?: "unknown"
    )
}