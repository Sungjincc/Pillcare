package com.example.pillcare_capstone.data_class

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("token") val fcmToken: String
)