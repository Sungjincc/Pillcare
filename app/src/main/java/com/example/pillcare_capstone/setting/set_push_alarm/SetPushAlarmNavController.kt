package com.example.pillcare_capstone.setting.set_push_alarm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pillcare_capstone.setting.components.ChangeSuccess

@Composable
fun SetPushAlarmNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "set_push_alarm") {
        composable("set_push_alarm") {
            SetPushAlarmShow(navController)
        }
        composable("set_push_alarm_success") {
            ChangeSuccess()
        }
    }
}