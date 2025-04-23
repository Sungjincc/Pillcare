package com.example.pillcare_capstone.data_class

class MedicineTimePlus (
    var medicineName: String = "",
    var alarmTime: String = "",
    var selectedDays: MutableList<String> = mutableListOf(),
    var timeList: MutableList<MedicineTimePlusTime> = mutableListOf()
    )

