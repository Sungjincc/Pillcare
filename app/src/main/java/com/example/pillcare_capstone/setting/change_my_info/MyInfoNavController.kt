package com.example.pillcare_capstone.setting.change_my_info

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pillcare_capstone.setting.components.ChangeSuccess

@Composable
fun MyInfoNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "change_my_info") {
        composable("change_my_info") {
            ChangeMyInfoShow(navController)
        }
        composable("change_my_info_success") {
            ChangeSuccess()
        }
    }
}