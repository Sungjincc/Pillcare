package com.example.pillcare_capstone.sign_up

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.pillcare_capstone.login.LoginActivity
import com.example.pillcare_capstone.R

class SignUpActivityThree : AppCompatActivity() {

    private lateinit var  signUpSuccessButton : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_three)


        initViews()
        initListeners()

    }

    private fun initViews()
    {
        signUpSuccessButton = findViewById(R.id.signUpSuccessButton)
    }

    private fun initListeners()
    {
        signUpSuccessButton.setOnClickListener{
            val intent = Intent(this@SignUpActivityThree, LoginActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }
}