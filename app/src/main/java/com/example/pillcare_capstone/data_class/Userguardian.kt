package com.example.pillcare_capstone.data_class

import com.google.gson.annotations.SerializedName

data class Userguardian(
    @SerializedName("name") val name: String = "",
    @SerializedName("ID") val ID: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("careTargetName") val careTargetName: String = "",
    @SerializedName("careTargetPhoneNumber") val careTargetPhoneNumber: String = "",
    @SerializedName("guardianMemos") val guardianMemos: List<GuardianMemo> = listOf()
)
