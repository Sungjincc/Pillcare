package com.example.pillcare_capstone.sign_up

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.adapter.*
import com.example.pillcare_capstone.data_class.GuardianMemo
import com.example.pillcare_capstone.data_class.UserInformation
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivityTwo : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var guardianMemoPlusButton: ImageButton
    private lateinit var careTargetSuccessButton: AppCompatButton
    private lateinit var adapter: GuardianMemoAdapter

    private lateinit var careTargetNameEditText : EditText
    private lateinit var careTargetPhoneNumberEditText : EditText
    private val guardianMemoList = mutableListOf(GuardianMemo())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_two)

        initView()
        initRecyclerView()
        initListeners()

        val name = intent.getStringExtra("name") ?: ""
        val userId = intent.getStringExtra("userId") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val careTargetName = careTargetNameEditText.text.toString()
        val careTargetPhoneNumber = careTargetPhoneNumberEditText.text.toString()
        val guardianMemoList = adapter.getMemoList()

        val user = UserInformation(
            name = name,
            userId = userId,
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

    private fun initView() {
        recyclerView = findViewById(R.id.guardianMemoPlusRecyclerView)
        guardianMemoPlusButton = findViewById(R.id.guardianMemoPlusButton)
        careTargetSuccessButton = findViewById(R.id.careTargetSuccessButton)
        careTargetNameEditText = findViewById(R.id.careTargetNameEditText)
        careTargetPhoneNumberEditText = findViewById(R.id.careTargetPhoneNumberEditText)
    }

    private fun initRecyclerView() {
        adapter = GuardianMemoAdapter(guardianMemoList, layoutInflater)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun initListeners() {
        guardianMemoPlusButton.setOnClickListener {
            adapter.addItem()
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
        careTargetSuccessButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivityThree::class.java))
        }
    }

}
