package com.example.pillcare_capstone.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.pillcare_capstone.R

object DialogUtils {

    fun showLimitReachedDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("약통 부족")
            .setMessage("약은 최대 3개까지만 등록할 수 있습니다.")
            .setPositiveButton("확인", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(context, R.color.textMainColor)
            )
        }

        dialog.show()
    }
}