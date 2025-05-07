package com.example.pillcare_capstone.setting.set_push_alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.pillcare_capstone.setting.change_password.PasswordNavController

class SetPushAlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetPushAlarmNavController()
        }
    }
}