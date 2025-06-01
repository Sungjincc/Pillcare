package com.example.pillcare_capstone.data_class

//내 정보 조회 guardianMemos클래스
data class GuardianMemosResponse(
    var userId: Int,
    var memoId: Int,
    var content: String = ""
)
