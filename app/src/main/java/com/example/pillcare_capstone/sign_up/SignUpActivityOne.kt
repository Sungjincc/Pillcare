package com.example.pillcare_capstone.sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pillcare_capstone.databinding.ActivitySignUpOneBinding
import com.example.pillcare_capstone.network.IdCheckRepository
import com.example.pillcare_capstone.utils.DialogUtils
import kotlinx.coroutines.launch

class SignUpActivityOne : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpOneBinding
    private var isIdAvailable = false  // ✅ 중복 확인 여부 저장 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpOneBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupFilters()
        setupTextWatchers()
        setupListeners()
        setupImeActions()
    }

    private fun setupFilters() {
        val noSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.contains(" ")) {
                ""  // 공백 제거는 OK
            } else {
                null
            }
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
                isIdAvailable = false
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
        binding.buttonCheckDuplicate.setOnClickListener {
            val inputSignUpId = binding.signUpIdEditText.text.toString().trim()

            if (inputSignUpId.isEmpty()) {
                DialogUtils.showAlertDialog(this, message = "아이디를 입력해주세요.")
                return@setOnClickListener
            }

            val idPattern = "^[a-zA-Z0-9]{5,20}$".toRegex()
            if (!idPattern.matches(inputSignUpId)) {
                DialogUtils.showAlertDialog(
                    this,
                    title = "알림",
                    message = "아이디는 5~20자의 영문 또는 숫자만 사용할 수 있습니다."
                )
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = IdCheckRepository.checkDuplicateId(inputSignUpId)
                    if (response.isSuccessful) {
                        val exists = response.body()?.exists == true
                        if (exists) {
                            DialogUtils.showAlertDialog(this@SignUpActivityOne, message = "이미 사용 중인 아이디입니다.")
                            isIdAvailable = false
                        } else {
                            DialogUtils.showAlertDialog(this@SignUpActivityOne, message = "사용 가능한 아이디입니다.")
                            isIdAvailable = true
                        }
                    } else {
                        DialogUtils.showAlertDialog(this@SignUpActivityOne, message = "서버 오류입니다.")
                    }
                } catch (e: Exception) {
                    DialogUtils.showAlertDialog(this@SignUpActivityOne, message = "네트워크 오류: ${e.message}")
                }
            }
        }

        binding.signUpNextButton.setOnClickListener {
            val inputSignUpName = binding.signUpNameEditText.text.toString().trim()
            val inputSignUpId = binding.signUpIdEditText.text.toString().trim()
            val inputSignUpPhoneNumber = binding.signUpPhoneNumberEditText.text.toString().trim()
            val inputSignUpVerificationCode = binding.signUpVerificationCodeEditText.text.toString().trim()
            val inputSignUpPassword = binding.signUpPasswordEditText.text.toString().trim()
            val inputSignUpconfirmPassword = binding.signUpConfirmPasswordEditText.text.toString().trim()

            when {
                inputSignUpName.isEmpty() -> {
                    DialogUtils.showAlertDialog(this, message = "이름을 입력해주세요.")
                    return@setOnClickListener
                }
                inputSignUpId.isEmpty() -> {
                    DialogUtils.showAlertDialog(this, message = "아이디를 입력해주세요.")
                    return@setOnClickListener
                }
//                !isIdAvailable -> {
//                    DialogUtils.showAlertDialog(this, message = "아이디 중복 확인을 완료해주세요.")
//                    return@setOnClickListener
//                }
                inputSignUpPhoneNumber.isEmpty() -> {
                    DialogUtils.showAlertDialog(this, message = "전화번호를 입력해주세요.")
                    return@setOnClickListener
                }
                inputSignUpVerificationCode.isEmpty() -> {
                    DialogUtils.showAlertDialog(this, message = "인증번호를 입력해주세요.")
                    return@setOnClickListener
                }
                inputSignUpPassword.isEmpty() -> {
                    DialogUtils.showAlertDialog(this, message = "비밀번호를 입력해주세요.")
                    return@setOnClickListener
                }
                inputSignUpconfirmPassword.isEmpty() -> {
                    DialogUtils.showAlertDialog(this, message = "비밀번호 확인을 입력해주세요.")
                    return@setOnClickListener
                }
                inputSignUpPassword != inputSignUpconfirmPassword -> {
                    DialogUtils.showAlertDialog(this, message = "비밀번호가 일치하지 않습니다.")
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, SignUpActivityTwo::class.java).apply {
                putExtra("name", binding.signUpNameEditText.text.toString())
                putExtra("userId", binding.signUpIdEditText.text.toString())
                putExtra("phoneNumber", binding.signUpPhoneNumberEditText.text.toString())
                putExtra("password", binding.signUpPasswordEditText.text.toString())
            }
            startActivity(intent)
        }
    }

    private fun setupImeActions() {
        binding.signUpNameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.signUpIdEditText.requestFocus()
                true
            } else false
        }

        binding.signUpIdEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.signUpConfirmPasswordEditText.windowToken, 0)
                true
            } else false
        }

        binding.signUpPhoneNumberEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.signUpConfirmPasswordEditText.windowToken, 0)
                true
            } else false
        }

        binding.signUpVerificationCodeEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.signUpConfirmPasswordEditText.windowToken, 0)
                true
            } else false
        }

        binding.signUpPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.signUpConfirmPasswordEditText.windowToken, 0)
                true
            } else false
        }

        binding.signUpConfirmPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.signUpConfirmPasswordEditText.windowToken, 0)
                true
            } else false
        }
    }
}