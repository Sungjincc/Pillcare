package com.example.pillcare_capstone.sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.databinding.ActivitySignUpOneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivityOne : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpOneBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFilters()
        setupTextWatchers()
        setupListeners()
    }

    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) "" else source
        }

        binding.signUpNameEditText.filters = arrayOf(noSpaceFilter)
        binding.signUpIdEditText.filters = arrayOf(noSpaceFilter)
        binding.signUpPasswordEditText.filters = arrayOf(noSpaceFilter)
        binding.signUpConfirmPasswordEditText.filters = arrayOf(noSpaceFilter)
    }

    private fun setupTextWatchers() {
        binding.signUpIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val minLength = 5
                val maxLength = 20
                binding.signUpIdLayout.error = when {
                    s.isNullOrBlank() || s.length < minLength -> "최소 ${minLength}자 이상 입력해주세요."
                    s.length > maxLength -> "최대 ${maxLength}자 까지 입력해주세요."
                    else -> null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.signUpVerificationCodeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 6) {
                    binding.signUpVerificationCodeEditText.isEnabled = false
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.signUpVerificationCodeEditText.windowToken, 0)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupListeners() {
        binding.signUpNextButton.setOnClickListener {
            val intent = Intent(this, SignUpActivityTwo::class.java).apply {
                putExtra("name", binding.signUpNameEditText.text.toString())
                putExtra("userId", binding.signUpIdEditText.text.toString())
                putExtra("phoneNumber", binding.signUpPhoneNumberEditText.text.toString())
                putExtra("password", binding.signUpPasswordEditText.text.toString())
            }
            startActivity(intent)
        }
    }
}