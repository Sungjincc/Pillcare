package com.example.pillcare_capstone.find_password

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.pillcare_capstone.R

class FindPasswordActivityTwo : AppCompatActivity() {

    private lateinit var changePasswordEditText : EditText
    private lateinit var confirmChangePasswordEditText : EditText
    private lateinit var changePasswordNextButton : AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password_two)
        initViews()
        setupListeners()
        setupFilters()
    }

    private fun initViews()
    {
        changePasswordEditText =findViewById(R.id.changePasswordEditText)
        confirmChangePasswordEditText = findViewById(R.id.confirmChangePasswordEditText)
        changePasswordNextButton = findViewById(R.id.changePasswordNextButton)
    }

    private fun setupListeners(){
        changePasswordNextButton.setOnClickListener {
            val intent =Intent(this@FindPasswordActivityTwo,FindPasswordActivityThree::class.java)
            startActivity(intent)
        }
    }


    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) "" else source
        }
        changePasswordEditText.filters = arrayOf(noSpaceFilter)
        confirmChangePasswordEditText.filters = arrayOf(noSpaceFilter)
    }
}