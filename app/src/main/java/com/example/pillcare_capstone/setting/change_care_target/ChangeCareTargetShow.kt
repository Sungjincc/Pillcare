package com.example.pillcare_capstone.setting.change_care_target

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
fun ChangeCareTargetShow(navController: NavController)
{
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var guardianMemo by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp, start = 50.dp, end = 50.dp)
    ) {
        CustomTitleText("케어 대상자 정보 변경")

        CustomText("이름")

        CustomEditText(
            value = name,
            onValueChange = { name = it },
            label = "이름"
        )

        CustomText("전화번호")
        CustomEditText(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "전화번호"
        )
        CustomText("보호자 메모")
        CustomEditText(
            value = guardianMemo,
            onValueChange = { guardianMemo = it },
            label = "보호자 메모"
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
                navController.navigate("change_care_target_success")
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.popBackStack()
                    activity?.finish()
                }, 2000)
            }, "완료")
        }
    }
}

