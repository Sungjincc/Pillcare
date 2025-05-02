package com.example.pillcare_capstone.setting.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun CustomText(
    value : String
)
{
    Text(
        text = value,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color(0xFF085408)
    )
}
