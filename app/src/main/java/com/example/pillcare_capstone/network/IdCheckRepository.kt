package com.example.pillcare_capstone.network

import com.example.pillcare_capstone.data_class.CheckIdResponse
import retrofit2.Response

object IdCheckRepository {
    suspend fun checkDuplicateId(inputId: String): Response<CheckIdResponse> {
        return RetrofitClient.apiService.checkDuplicateId(inputId)
    }
}