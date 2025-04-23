package com.example.pillcare_capstone.find_password

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.login.LoginActivity

class FindPasswordActivityThree : AppCompatActivity() {

    private lateinit var changePasswordSuccessButton : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password_three)

        initViews()
        setupListeners()
    }

    private fun initViews()
    {
        changePasswordSuccessButton = findViewById(R.id.changePasswordSuccessButton)
    }

    private fun setupListeners()
    {
        changePasswordSuccessButton.setOnClickListener {
            val intent = Intent(this@FindPasswordActivityThree,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}