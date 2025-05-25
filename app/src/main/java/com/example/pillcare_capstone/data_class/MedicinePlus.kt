package com.example.pillcare_capstone.data_class

class MedicinePlus (
    var medicineName: String = "",
    var selectedDays: MutableList<String> = mutableListOf(),
    var timeList: MutableList<MedicineTimePlus> = mutableListOf()
    )

