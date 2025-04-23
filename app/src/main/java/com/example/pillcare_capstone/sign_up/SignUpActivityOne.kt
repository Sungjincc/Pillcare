package com.example.pillcare_capstone.sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.pillcare_capstone.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivityOne : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var signUpNameLayout: TextInputLayout
    private lateinit var signUpNameEditText: EditText
    private lateinit var signUpIdLayout: TextInputLayout
    private lateinit var signUpIdEditText: EditText
    private lateinit var signUpPhoneNumberEditText :EditText
    private lateinit var signUpVerificationEditText: EditText
    private lateinit var signUpPasswordEditText: EditText
    private lateinit var signUpConfirmPasswordEditText: EditText
    private lateinit var signUpNextButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_one)

        initViews()
        setupFilters()
        setupTextWatchers()
        setupListeners()
    }

    private fun initViews() {
        signUpNameLayout = findViewById(R.id.signUpNameLayout)
        signUpNameEditText = findViewById(R.id.signUpNameEditText)
        signUpIdLayout = findViewById(R.id.signUpIdLayout)
        signUpIdEditText = findViewById(R.id.signUpIdEditText)
        signUpPhoneNumberEditText = findViewById(R.id.signUpPhoneNumberEditText)
        signUpVerificationEditText = findViewById(R.id.signUpVerificationCodeEditText)

        val signUpPasswordLayout = findViewById<TextInputLayout>(R.id.signUpPasswordLayout)
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText)

        val signUpConfirmPasswordLayout = findViewById<TextInputLayout>(R.id.signUpConfirmPasswordLayout)
        signUpConfirmPasswordEditText = findViewById(R.id.signUpConfirmPasswordEditText)

        signUpNextButton = findViewById(R.id.signUpNextButton)
    }

    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) "" else source
        }
        signUpNameEditText.filters = arrayOf(noSpaceFilter)
        signUpIdEditText.filters = arrayOf(noSpaceFilter)
        signUpPasswordEditText.filters = arrayOf(noSpaceFilter)
        signUpConfirmPasswordEditText.filters = arrayOf(noSpaceFilter)
    }

    private fun setupTextWatchers() {
        signUpIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val minLength = 5
                val maxLength = 20
                signUpIdLayout.error = when {
                    s.isNullOrBlank() || s.length < minLength -> "최소 ${minLength}자 이상 입력해주세요."
                    s.length > maxLength -> "최대 ${maxLength}자 까지 입력해주세요."
                    else -> null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        signUpVerificationEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 6) {
                    signUpVerificationEditText.isEnabled = false
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(signUpVerificationEditText.windowToken, 0)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupListeners() {
        signUpNextButton.setOnClickListener {
            val intent = Intent(this, SignUpActivityTwo::class.java)
            intent.putExtra("name",signUpNameEditText.text.toString())
            intent.putExtra("userId", signUpIdEditText.text.toString())
            intent.putExtra("phoneNumber", signUpPhoneNumberEditText.text.toString())
            intent.putExtra("password", signUpPasswordEditText.text.toString())
            startActivity(intent)
        }
    }
}