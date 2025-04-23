package com.example.pillcare_capstone.data_class

data class UserInformation (
    var name: String = "",
    var userId: String = "",
    var phoneNumber: String = "",
    var password: String = "",
    var careTargetName: String = "",
    var careTargetPhoneNumber: String = "",
    var guardianMemos: List<GuardianMemo> = listOf()
)