package com.example.pillcare_capstone.data_class

data class UpdateGuardianRequest(
    val name: String,
    val phoneNumber: String,
    val careTargetName: String,
    val careTargetPhoneNumber: String
)
