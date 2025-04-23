package com.example.pillcare_capstone.find_password

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.pillcare_capstone.R
import com.google.android.material.textfield.TextInputLayout

class FindPasswordActivityOne : AppCompatActivity() {

    private lateinit var findPasswordIdEditText : EditText
    private lateinit var findPasswordPhoneNumberEditText : EditText
    private lateinit var findPasswordVerificationCodeEditText : EditText
    private lateinit var findPasswordNextButton : AppCompatButton
    private lateinit var findPasswordIdLayout : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password_one)

        initViews()
        initListeners()
        setupFilters()
        setupTextWatchers()

    }

    private fun initViews()
    {
        findPasswordIdEditText = findViewById(R.id.findPasswordIdEditText)
        findPasswordPhoneNumberEditText = findViewById(R.id.findPasswordPhoneNumberEditText)
        findPasswordVerificationCodeEditText = findViewById(R.id.findPasswordVerificationCodeEditText)
        findPasswordNextButton = findViewById(R.id.findPasswordNextButton)
        findPasswordIdLayout = findViewById(R.id.findPasswordIdLayout)

    }

    private fun initListeners()
    {
        findPasswordNextButton.setOnClickListener {
            val intent = Intent(this@FindPasswordActivityOne, FindPasswordActivityTwo::class.java)
            startActivity(intent)
        }
    }
    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) "" else source
        }
        findPasswordIdEditText.filters = arrayOf(noSpaceFilter)
        findPasswordPhoneNumberEditText.filters = arrayOf(noSpaceFilter)
        findPasswordVerificationCodeEditText.filters = arrayOf(noSpaceFilter)
    }

    private fun setupTextWatchers() {
        findPasswordIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val minLength = 5
                val maxLength = 20
                findPasswordIdLayout.error = when {
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