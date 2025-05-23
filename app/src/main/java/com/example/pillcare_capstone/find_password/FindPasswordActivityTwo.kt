package com.example.pillcare_capstone.find_password

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.databinding.ActivityFindPasswordTwoBinding

class FindPasswordActivityTwo : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupFilters()
    }

    private fun setupListeners() {
        binding.changePasswordNextButton.setOnClickListener {
            val intent = Intent(this, FindPasswordActivityThree::class.java)
            startActivity(intent)
        }
    }

    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) "" else source
        }

        binding.changePasswordEditText.filters = arrayOf(noSpaceFilter)
        binding.confirmChangePasswordEditText.filters = arrayOf(noSpaceFilter)
    }
}