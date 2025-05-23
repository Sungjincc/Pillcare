package com.example.pillcare_capstone.find_password

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.databinding.ActivityFindPasswordThreeBinding
import com.example.pillcare_capstone.login.LoginActivity

class FindPasswordActivityThree : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.changePasswordSuccessButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}