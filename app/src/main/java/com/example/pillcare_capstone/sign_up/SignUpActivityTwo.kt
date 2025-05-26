package com.example.pillcare_capstone.sign_up

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillcare_capstone.adapter.GuardianMemoAdapter
import com.example.pillcare_capstone.data_class.GuardianMemo
import com.example.pillcare_capstone.data_class.Userguardian
import com.example.pillcare_capstone.databinding.ActivitySignUpTwoBinding
import com.example.pillcare_capstone.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivityTwo : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpTwoBinding
    private lateinit var adapter: GuardianMemoAdapter
    private val guardianMemoList = mutableListOf(GuardianMemo())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        adapter = GuardianMemoAdapter(guardianMemoList, layoutInflater)
        binding.guardianMemoPlusRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.guardianMemoPlusRecyclerView.adapter = adapter
        binding.guardianMemoPlusRecyclerView.setHasFixedSize(true)
        binding.guardianMemoPlusRecyclerView.isNestedScrollingEnabled = false
    }

    private fun initListeners() {
        binding.guardianMemoPlusButton.setOnClickListener {
            adapter.addItem()
            binding.guardianMemoPlusRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        binding.careTargetSuccessButton.setOnClickListener {
            // 사용자 정보 수신
            val name = intent.getStringExtra("name") ?: ""
            val userId = intent.getStringExtra("userId") ?: ""
            val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
            val password = intent.getStringExtra("password") ?: ""

            // 보호 대상자 정보
            val careTargetName = binding.careTargetNameEditText.text.toString()
            val careTargetPhoneNumber = binding.careTargetPhoneNumberEditText.text.toString()
            val guardianMemoList = adapter.getMemoList()

            // 서버로 전송할 객체 생성
            val user = Userguardian(
                name = name,
                ID = userId,
                phoneNumber = phoneNumber,
                password = password,
                careTargetName = careTargetName,
                careTargetPhoneNumber = careTargetPhoneNumber,
                guardianMemos = guardianMemoList
            )

            // Retrofit으로 서버 전송
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val gson = Gson()
                    val jsonBody = gson.toJson(user)
                    Log.d("전송되는 JSON", jsonBody)
                    val response = RetrofitClient.apiService.registerGuardian(user)
                    if (response.isSuccessful) {
                        Log.d("Server", "회원가입 서버 전송 성공")
                        runOnUiThread {
                            startActivity(Intent(this@SignUpActivityTwo, SignUpActivityThree::class.java))
                        }
                    } else {
                        Log.e("Server", "회원가입 실패: ${response.code()} ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("Server", "네트워크 오류: ${e.localizedMessage}")
                }
            }
        }
    }
}