package com.example.pillcare_capstone.data_class

data class UpdateCareTargetRequest(
    val careTargetName: String,
    val careTargetPhoneNumber: String,
    val guardianMemos: List<String>
)