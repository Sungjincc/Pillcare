package com.example.pillcare_capstone.setting.change_care_target

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pillcare_capstone.data_class.UpdateCareTargetRequest
import com.example.pillcare_capstone.network.RetrofitClient
import com.example.pillcare_capstone.setting.components.CustomButton
import com.example.pillcare_capstone.setting.components.CustomEditText
import com.example.pillcare_capstone.setting.components.CustomText
import com.example.pillcare_capstone.setting.components.CustomTitleText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChangeCareTargetShow(navController: NavController, userId: Int) {
    var careTargetName by remember { mutableStateOf("") }
    var careTargetPhoneNumber by remember { mutableStateOf("") }
    val memoList = remember { mutableStateListOf<String>() }

    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(userId) {
        val response = RetrofitClient.apiService.getUserInfo(userId)
        if (response.isSuccessful) {
            val user = response.body()
            user?.let {
                careTargetName = it.careTargetName
                careTargetPhoneNumber = it.careTargetPhoneNumber.replace("-", "")
                memoList.clear()
                memoList.addAll(it.guardianMemos?.map { memo -> memo.content }.orEmpty())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 130.dp, start = 50.dp, end = 50.dp)
    ) {
        CustomTitleText("케어 대상자 정보 변경")

        CustomText("이름")

        CustomEditText(
            value = careTargetName,
            onValueChange = { careTargetName = it },
            label = "이름"
        )

        CustomText("전화번호")

        CustomEditText(
            value = careTargetPhoneNumber,
            onValueChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                if (digitsOnly.length <= 11) {
                    careTargetPhoneNumber = digitsOnly
                }
            },
            label = "전화번호"
        )

        CustomText("보호자 메모")

        memoList.forEachIndexed { index, memoContent ->
            CustomEditText(
                value = memoContent,
                onValueChange = { newText -> memoList[index] = newText },
                label = "보호자 메모 ${index + 1}"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomButton(
                onClick = { memoList.add("") },
                value = "메모 추가",
                fontSize = 12
            )

            if (memoList.isNotEmpty()) {
                CustomButton(
                    onClick = { memoList.removeLastOrNull() },
                    value = "메모 삭제",
                    fontSize = 12,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(36.dp, Alignment.CenterHorizontally)
        ) {
            CustomButton(onClick = {
                activity?.finish()
            }, value = "이전")

            CustomButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = RetrofitClient.apiService.updateCareTarget(
                        userId = userId,
                        request = UpdateCareTargetRequest(
                            careTargetName = careTargetName,
                            careTargetPhoneNumber = careTargetPhoneNumber,
                            guardianMemos = memoList.toList()
                        )
                    )
                    Handler(Looper.getMainLooper()).post {
                        if (result.isSuccessful) {
                            Toast.makeText(context, "정보 수정 완료", Toast.LENGTH_SHORT).show()
                            navController.navigate("change_care_target_success")
                            Handler(Looper.getMainLooper()).postDelayed({
                                navController.popBackStack()
                                activity?.finish()
                            }, 2000)
                        } else {
                            Toast.makeText(context, "수정 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, value = "완료")
        }
    }
}