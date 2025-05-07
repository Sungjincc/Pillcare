package com.example.pillcare_capstone.setting.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTitleText(
    value : String
) {
    Text(
        text = value,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
    )
}

