package com.example.pillcare_capstone.setting.change_care_target

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ChangeCareTargetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getIntExtra("userId", -1)
        setContent {
            CareTargetNavController(userId =userId)
        }
    }
}