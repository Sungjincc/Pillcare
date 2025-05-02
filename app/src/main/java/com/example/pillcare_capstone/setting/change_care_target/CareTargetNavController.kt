package com.example.pillcare_capstone.setting.change_care_target

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pillcare_capstone.setting.change_my_info.ChangeMyInfoShow
import com.example.pillcare_capstone.setting.components.ChangeSuccess

@Composable
fun CareTargetNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "change_care_target") {
        composable("change_care_target") {
            ChangeCareTargetShow(navController)
        }
        composable("change_care_target_success") {
            ChangeSuccess()
        }
    }
}