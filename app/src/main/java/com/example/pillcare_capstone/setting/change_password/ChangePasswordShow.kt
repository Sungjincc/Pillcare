package com.example.pillcare_capstone.setting.change_password

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pillcare_capstone.data_class.PasswordCheckRequest
import com.example.pillcare_capstone.data_class.ResetPasswordRequest
import com.example.pillcare_capstone.network.RetrofitClient
import com.example.pillcare_capstone.setting.components.CustomButton
import com.example.pillcare_capstone.setting.components.CustomEditText
import com.example.pillcare_capstone.setting.components.CustomText
import com.example.pillcare_capstone.setting.components.CustomTitleText
import com.example.pillcare_capstone.utils.DialogUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordShow(navController: NavController, userId: Int) {
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
            onValueChange = { newPassword = it },
            label = "새 비밀번호 입력",
            isPassword = true
        )

        CustomText("새 비밀번호 재입력")

        CustomEditText(
            value = confirmNewPassword,
            onValueChange = { confirmNewPassword = it },
            label = "새 비밀번호 재입력",
            isPassword = true
        )

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
                when {
                    beforePassword.isEmpty() -> {
                        DialogUtils.showAlertDialog(
                            context,
                            title = "오류",
                            message = "기존 비밀번호를 입력해주세요."
                        )
                    }
                    newPassword != confirmNewPassword -> {
                        DialogUtils.showAlertDialog(
                            context,
                            title = "오류",
                            message = "새 비밀번호가 일치하지 않습니다."
                        )
                    }
                    newPassword.length < 6 -> {
                        DialogUtils.showAlertDialog(
                            context,
                            title = "오류",
                            message = "비밀번호는 최소 6자 이상이어야 합니다."
                        )
                    }
                    else -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                // 먼저 기존 비밀번호 검증
                                val checkResult = RetrofitClient.apiService.checkPassword(
                                    userId = userId,
                                    request = PasswordCheckRequest(beforePassword)
                                )
                                
                                Log.d("PasswordCheck", "Response Code: ${checkResult.code()}")
                                Log.d("PasswordCheck", "Is Successful: ${checkResult.isSuccessful}")
                                Log.d("PasswordCheck", "Response Body: ${checkResult.body()}")
                                
                                Handler(Looper.getMainLooper()).post {
                                    if (checkResult.isSuccessful && checkResult.body() == true) {
                                        // 비밀번호가 일치하면 새 비밀번호로 변경
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val result = RetrofitClient.apiService.resetPassword(
                                                userId = userId,
                                                request = ResetPasswordRequest(newPassword)
                                            )
                                            Handler(Looper.getMainLooper()).post {
                                                if (result.isSuccessful) {
                                                    DialogUtils.showAlertDialog(
                                                        context,
                                                        title = "완료",
                                                        message = "비밀번호가 변경되었습니다."
                                                    )
                                                    navController.navigate("change_password_success")
                                                    Handler(Looper.getMainLooper()).postDelayed({
                                                        navController.popBackStack()
                                                        activity?.finish()
                                                    }, 2000)
                                                } else {
                                                    DialogUtils.showAlertDialog(
                                                        context,
                                                        title = "실패",
                                                        message = "변경 실패. 다시 시도해주세요."
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        DialogUtils.showAlertDialog(
                                            context,
                                            title = "오류",
                                            message = "기존 비밀번호가 일치하지 않습니다."
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("PasswordCheck", "Exception occurred: ${e.message}")
                                Log.e("PasswordCheck", "Exception type: ${e.javaClass.simpleName}")
                                Log.e("PasswordCheck", "Stack trace: ${e.stackTraceToString()}")
                                
                                Handler(Looper.getMainLooper()).post {
                                    DialogUtils.showAlertDialog(
                                        context,
                                        title = "오류",
                                        message = "네트워크 오류가 발생했습니다.\n${e.message}"
                                    )
                                }
                            }
                        }
                    }
                }
            }, value = "완료")
        }
    }
}
