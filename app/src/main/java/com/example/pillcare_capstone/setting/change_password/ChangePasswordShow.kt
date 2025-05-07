package com.example.pillcare_capstone.setting.change_password

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pillcare_capstone.setting.components.CustomButton
import com.example.pillcare_capstone.setting.components.CustomEditText
import com.example.pillcare_capstone.setting.components.CustomText
import com.example.pillcare_capstone.setting.components.CustomTitleText

@Composable
fun ChangePasswordShow(navController: NavController) {
    var beforePassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp, start = 50.dp, end = 50.dp)
    ) {
        CustomTitleText("비밀번호 변경")

        CustomText("기존 비밀번호")

        CustomEditText(
            value = beforePassword,
            onValueChange = { beforePassword = it },
            label = "기존 비밀번호",
            isPassword = true
        )

        CustomText("새 비밀번호 입력")

        CustomEditText(
            value = newPassword,
            onValueChange = { newPassword = it},
            label = "새 비밀번호 입력",
            isPassword = true
        )
        CustomText("새 비밀번호 재입력")

        CustomEditText(
            value = confirmNewPassword,
            onValueChange = { confirmNewPassword =it },
            label = "새 비밀번호 재입력",
            isPassword = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(36.dp, Alignment.CenterHorizontally)
        ){
            CustomButton(onClick = {
                activity?.finish()
            },"이전")

            CustomButton(onClick = {
                navController.navigate("change_password_success")
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.popBackStack()
                    activity?.finish()
                }, 2000)
            }, "완료")
        }

    }
}