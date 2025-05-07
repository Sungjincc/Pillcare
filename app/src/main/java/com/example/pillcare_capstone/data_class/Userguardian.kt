package com.example.pillcare_capstone.data_class

data class Userguardian (
    var name: String = "",
    var g_userId: Int = 0, //추가 보호자 아이디 : 식별
    var userId : Int = 0, // 추가 유저 아이디(환자 아이디:식별)
    var ID: String = "", // userId -> ID로 : 로그인 아이디
    var phoneNumber: String = "",
    var password: String = "",
    var careTargetName: String = "",
    var careTargetPhoneNumber: String = "",
    var guardianMemos: List<GuardianMemo> = listOf()
)