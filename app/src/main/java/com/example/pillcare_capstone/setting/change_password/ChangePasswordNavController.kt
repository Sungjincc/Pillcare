package com.example.pillcare_capstone.setting.change_password

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pillcare_capstone.setting.components.ChangeSuccess

@Composable
fun PasswordNavController(userId : Int) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "change_password") {
        composable("change_password") {
            ChangePasswordShow(navController,userId)
        }
        composable("change_password_success") {
            ChangeSuccess()
        }
    }
}