package com.example.pillcare_capstone.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pillcare_capstone.MainActivity
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.find_password.FindPasswordActivityOne
import com.example.pillcare_capstone.sign_up.SignUpActivityOne


class LoginActivity : AppCompatActivity() {

    private lateinit var findPasswordTextView : TextView
    private lateinit var loginButton : Button
    private lateinit var signUpLayout : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        initListeners()
    }
    private fun initViews()
    {
        findPasswordTextView = findViewById(R.id.findPasswordTextView)
        loginButton = findViewById(R.id.signUpSuccessButton)
        signUpLayout = findViewById(R.id.signupLayout)
    }
    private fun initListeners()
    {
        signUpLayout.setOnClickListener{

            val intent = Intent(this@LoginActivity, SignUpActivityOne::class.java)
            startActivity(intent)
        }
        findPasswordTextView.setOnClickListener{
            val intent = Intent(this@LoginActivity, FindPasswordActivityOne::class.java)
            startActivity(intent)
        }
    }
}