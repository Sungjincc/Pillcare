package com.example.pillcare_capstone.utils


import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.data_class.MedicineTimePlus
import com.example.pillcare_capstone.data_class.PillCaseColor

object PillCaseColorSelector {

    fun showColorSelectionDialog(
        context: Context,
        usedColors: Set<PillCaseColor>,
        onSelected: (MedicinePlus) -> Unit
    ) {
        val allColors = PillCaseColor.values().toList()
        val availableColors = allColors.filterNot { usedColors.contains(it) }
        val colorDisplayNames = availableColors.map { it.displayName }.toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("약통 색상 선택")
            .setItems(colorDisplayNames) { _, which ->
                val selectedColor = availableColors[which]
                val newItem = MedicinePlus().apply {
                    pillCaseColor = selectedColor
                }
                onSelected(newItem)
            }
            .setCancelable(false)
            .show()
    }
}