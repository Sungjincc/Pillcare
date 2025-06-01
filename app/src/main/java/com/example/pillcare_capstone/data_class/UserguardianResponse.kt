package com.example.pillcare_capstone.data_class

data class UserguardianResponse(
    val userId: Int,
    val ID: String,
    val name: String,
    val phoneNumber: String,
    val careTargetName: String,
    val careTargetPhoneNumber: String,
    val guardianMemos: List<GuardianMemosResponse>
)
