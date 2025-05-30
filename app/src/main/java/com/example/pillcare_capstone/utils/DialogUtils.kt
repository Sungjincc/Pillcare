package com.example.pillcare_capstone.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.pillcare_capstone.R

object DialogUtils {

    fun showAlertDialog(
        context: Context,
        title: String = "알림",
        message: String,
        positiveButtonText: String = "확인"
    ) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(context, R.color.textMainColor)
            )
        }

        dialog.show()
    }

    fun showLimitReachedDialog(context: Context) {
        showAlertDialog(
            context = context,
            title = "약통 부족",
            message = "약은 최대 3개까지만 등록할 수 있습니다."
        )
    }
}