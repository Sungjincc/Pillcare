package com.example.pillcare_capstone.sign_up

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.databinding.ActivitySignUpThreeBinding
import com.example.pillcare_capstone.login.LoginActivity

class SignUpActivityThree : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.signUpSuccessButton.setOnClickListener {
            val intent = Intent(this@SignUpActivityThree, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}