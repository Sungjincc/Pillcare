package com.example.pillcare_capstone.data_class

class MedicinePlus (
    var medicineName: String = "",
    var alarmTime: String = "",
    var selectedDays: MutableList<String> = mutableListOf(),
    var timeList: MutableList<MedicineTimePlus> = mutableListOf(),
    var pillCaseColor: PillCaseColor? = null,

    var isPosted: Boolean = false //앱 내에서만 사용할 변수임

)

