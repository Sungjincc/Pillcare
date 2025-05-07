package com.example.pillcare_capstone.setting.set_push_alarm

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.pillcare_capstone.setting.components.CustomButton
import com.example.pillcare_capstone.setting.components.CustomTitleText
import com.example.pillcare_capstone.setting.components.CustomToggle

@Composable
fun SetPushAlarmShow(navController: NavController)  {
    var pushEnabled by remember { mutableStateOf(false) }
    var timeAlarmEnabled by remember { mutableStateOf(true) }
    var takenAlarmEnabled by remember { mutableStateOf(true) }
    var missedAlarmEnabled by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val activity = context as? Activity

    Column(modifier = Modifier.padding(top = 130.dp, start = 50.dp, end = 50.dp)) {
        CustomTitleText("푸쉬 알림 설정")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomToggle(
                label = "알림 허용",
                checked = pushEnabled,
                onCheckedChange = { pushEnabled = it }
            )
        }

        if (pushEnabled) {
            Spacer(modifier = Modifier.height(12.dp))

            SettingSwitchRow(
                label = "약 시간 알림",
                checked = timeAlarmEnabled,
                onCheckedChange = { timeAlarmEnabled = it }
            )

            SettingSwitchRow(
                label = "약 복용 알림",
                checked = takenAlarmEnabled,
                onCheckedChange = { takenAlarmEnabled = it }
            )

            SettingSwitchRow(
                label = "약 미복용 알림",
                checked = missedAlarmEnabled,
                onCheckedChange = { missedAlarmEnabled = it }
            )
        }else {
            Spacer(modifier = Modifier.height(255.dp))
        }


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
                navController.navigate("set_push_alarm_success")
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.popBackStack()
                    activity?.finish()
                }, 2000)
            }, "완료")
        }
    }
}


@Composable
fun SettingSwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    CustomToggle(label,checked,onCheckedChange)
}