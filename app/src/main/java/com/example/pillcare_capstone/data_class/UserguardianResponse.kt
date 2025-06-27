package com.example.pillcare_capstone.data_class

import com.google.gson.annotations.SerializedName

data class UserguardianResponse(
    val userId: Int,

    @SerializedName("ID")
    val ID: String,

    val name: String,
    val phoneNumber: String,
    val careTargetName: String,
    val careTargetPhoneNumber: String,
    val guardianMemos: List<GuardianMemosResponse>
)
