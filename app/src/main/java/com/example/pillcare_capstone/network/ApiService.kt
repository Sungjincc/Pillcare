package com.example.pillcare_capstone.network



import com.example.pillcare_capstone.data_class.MedicineScheduleRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/medicine-schedule")
    suspend fun sendSchedule(@Body request: MedicineScheduleRequest): Response<Void>

    @PUT("api/medicine-schedule")
    suspend fun updateSchedule(@Body request: MedicineScheduleRequest): Response<Void>

    @DELETE("api/medicine-schedule/{id}")
    suspend fun deleteSchedule(@Path("id") medicineId: Int): Response<Void>
}