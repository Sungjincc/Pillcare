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
import com.google.firebase.firestore.FirebaseFirestore

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

        // 사용자 정보 수신
        val name = intent.getStringExtra("name") ?: ""
        val userId = intent.getStringExtra("userId") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        // 보호 대상자 정보
        val careTargetName = binding.careTargetNameEditText.text.toString()
        val careTargetPhoneNumber = binding.careTargetPhoneNumberEditText.text.toString()
        val guardianMemoList = adapter.getMemoList()

        // Firestore 저장
        val user = Userguardian(
            name = name,
            ID = userId,
            phoneNumber = phoneNumber,
            password = password,
            careTargetName = careTargetName,
            careTargetPhoneNumber = careTargetPhoneNumber,
            guardianMemos = guardianMemoList
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).set(user)
            .addOnSuccessListener { Log.d("Firestore", "저장 성공") }
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
            startActivity(Intent(this, SignUpActivityThree::class.java))
        }
    }
}