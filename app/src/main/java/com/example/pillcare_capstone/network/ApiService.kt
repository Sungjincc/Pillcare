package com.example.pillcare_capstone.network



import com.example.pillcare_capstone.data_class.CheckIdResponse
import com.example.pillcare_capstone.data_class.LoginRequest
import com.example.pillcare_capstone.data_class.LoginResponse
import com.example.pillcare_capstone.data_class.MedicineScheduleListResponse
import com.example.pillcare_capstone.data_class.MedicineScheduleRequest
import com.example.pillcare_capstone.data_class.MedicineScheduleUpdateRequest
import com.example.pillcare_capstone.data_class.PasswordResponse
import com.example.pillcare_capstone.data_class.ResetPasswordRequest
import com.example.pillcare_capstone.data_class.TokenRequest
import com.example.pillcare_capstone.data_class.UpdateCareTargetRequest
import com.example.pillcare_capstone.data_class.UpdateGuardianRequest
import com.example.pillcare_capstone.data_class.Userguardian
import com.example.pillcare_capstone.data_class.UserguardianResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //회원가입
    @POST("api/guardian/signup")
    suspend fun registerGuardian(@Body user: Userguardian): Response<Void>

    //회원가입 내 아이디 중복 체크
    @GET("api/guardian/check-id")
    suspend fun checkDuplicateId(
        @Query("ID") userId: String
    ): Response<CheckIdResponse>

    //로그인
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    //약 정보 전송
    @POST("api/medicine-schedule")
    suspend fun sendSchedule(@Body request: MedicineScheduleRequest): Response<Void>

    //약 정보 수정
    @PUT("api/medicine-schedule")
    suspend fun updateSchedule(@Body request: MedicineScheduleUpdateRequest): Response<Void>

    //약 정보 삭제
    @DELETE("api/medicine-schedule/{userId}/{pillCaseColor}")
    suspend fun deleteSchedule(
        @Path("userId") userId: Int,
        @Path("pillCaseColor") pillCaseColor: String
    ): Response<Void>

    //약 정보 조회
    @GET("api/medicine-schedule/{userId}")
    suspend fun getMedicineSchedules(
        @Path("userId") userId: Int
    ): Response<MedicineScheduleListResponse>

    //내 정보 조회
    @GET("/api/guardian/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: Int): Response<UserguardianResponse>

    //내 정보 수정
    @PUT("/api/guardian/{userId}")
    suspend fun updateUserInfo(
        @Path("userId") userId: Int,
        @Body request: UpdateGuardianRequest
    ): Response<Void>

    //케어 대상자 정보 수정
    @PUT("/api/guardian/care-target/{userId}")
    suspend fun updateCareTarget(
        @Path("userId") userId: Int,
        @Body request: UpdateCareTargetRequest
    ): Response<Void>

    //비밀번호 조회
    @GET("/api/guardian/password/{userId}")
    suspend fun getUserPassword(
        @Path("userId") userId: Int
    ): Response<PasswordResponse>

    //비밀번호 수정
    @PUT("/api/guardian/password/{userId}")
    suspend fun resetPassword(
        @Path("userId") userId: Int,
        @Body request: ResetPasswordRequest
    ): Response<Void>

    @POST("api/fcm/register")
    suspend fun sendFcmToken(@Body request: TokenRequest): Response<Void>

}
