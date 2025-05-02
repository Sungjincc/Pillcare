package com.example.pillcare_capstone.setting.change_my_info

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.pillcare_capstone.setting.components.CustomButton
import com.example.pillcare_capstone.setting.components.CustomEditText

import com.example.pillcare_capstone.setting.components.CustomText
import com.example.pillcare_capstone.setting.components.CustomTitleText


@Composable
fun ChangeMyInfoShow(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("dankook123") }
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember{ mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp, start = 50.dp, end = 50.dp)
    ) {
        CustomTitleText("내 정보 변경")

        CustomText("이름")

        CustomEditText(
            value = name,
            onValueChange = { name = it },
            label = "이름"
        )

        CustomText("아이디")

        CustomEditText(
            value = id,
            onValueChange = {},
            label = "아이디",
            enabled = false
        )
        CustomText("전화번호")


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            CustomEditText(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "전화번호",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            CustomButton(
                onClick = {
                    // 전화번호 인증 관련 기능 입력 필요
                },
                "인증번호 전송",
                modifier = Modifier
                    .padding(start = 8.dp),
                fontSize = 9
            )
        }

        CustomText("인증번호")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            CustomEditText(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = "인증번호",
                modifier = Modifier
                    .weight(1f) //
                    .padding(end = 8.dp) //
            )

            CustomButton(
                onClick = {
                    // 인증번호 확인 관련 기능 입력 필요
                },
                "인증번호 확인",
                modifier = Modifier
                    .padding(start = 8.dp),
                fontSize = 9
            )
        }

        CustomText("비밀번호")

        CustomEditText(
            value = password,
            onValueChange = { password = it },
            label = "비밀번호",
            enabled = false,
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
                navController.navigate("change_my_info_success")
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.popBackStack()
                    activity?.finish()
                }, 2000)
            }, "완료")
        }
    }
}