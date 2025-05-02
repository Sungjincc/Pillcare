package com.example.pillcare_capstone.setting.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    onClick: () -> Unit,
    value : String,
    modifier: Modifier = Modifier,
    fontSize: Int = 14
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(100.dp)
            .height(45.dp)
            .padding(5.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF7FCB83),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = fontSize.sp
        )
    }
}