package com.example.pillcare_capstone.find_password

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.databinding.ActivityFindPasswordOneBinding
import com.example.pillcare_capstone.R

class FindPasswordActivityOne : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        setupFilters()
        setupTextWatchers()
    }

    private fun initListeners() {
        binding.findPasswordNextButton.setOnClickListener {
            val intent = Intent(this@FindPasswordActivityOne, FindPasswordActivityTwo::class.java)
            startActivity(intent)
        }
    }

    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) "" else source
        }

        binding.findPasswordIdEditText.filters = arrayOf(noSpaceFilter)
        binding.findPasswordPhoneNumberEditText.filters = arrayOf(noSpaceFilter)
        binding.findPasswordVerificationCodeEditText.filters = arrayOf(noSpaceFilter)
    }

    private fun setupTextWatchers() {
        binding.findPasswordIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val minLength = 5
                val maxLength = 20
                binding.findPasswordIdLayout.error = when {
                    s.isNullOrBlank() || s.length < minLength -> "최소 ${minLength}자 이상 입력해주세요."
                    s.length > maxLength -> "최대 ${maxLength}자 까지 입력해주세요."
                    else -> null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}