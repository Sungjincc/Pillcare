package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillcare_capstone.adapter.GuardianMemoAdapter
import com.example.pillcare_capstone.data_class.GuardianMemo
import com.example.pillcare_capstone.data_class.UpdateGuardianRequest
import com.example.pillcare_capstone.data_class.UserguardianResponse
import com.example.pillcare_capstone.databinding.MyInfoFragmentBinding
import com.example.pillcare_capstone.network.RetrofitClient
import com.example.pillcare_capstone.utils.DialogUtils
import com.google.gson.Gson
import kotlinx.coroutines.launch


class MyInfoFragment : Fragment() {
    private var _binding: MyInfoFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var guardianMemoAdapter: GuardianMemoAdapter
    private val memoList = mutableListOf<GuardianMemo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // adapter를 먼저 연결
        guardianMemoAdapter = GuardianMemoAdapter(memoList, layoutInflater, GuardianMemoAdapter.ContextType.MY_INFO)
        binding.myInfoGuardianMemoRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = guardianMemoAdapter
        }

        val prefs = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        if (userId != -1) {
            loadUserInfo(userId)
        }

        binding.editButton.setOnClickListener {
            setEditMode(true)
        }

        binding.saveButton.setOnClickListener {
            updateUserInfo(userId)
        }
    }

    private fun loadUserInfo(userId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserInfo(userId)
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    userInfo?.let {
                        bindUserInfoToViews(it)
                    }
                } else {
                    DialogUtils.showAlertDialog(requireContext(), "알림", "서버 오류: 사용자 정보를 가져올 수 없습니다.")
                }
            } catch (e: Exception) {
                DialogUtils.showAlertDialog(requireContext(), "알림", "네트워크 오류: ${e.message}")
            }
        }
    }

    private fun bindUserInfoToViews(userInfo: UserguardianResponse) {
        binding.myInfoCompatButton.text = "${userInfo.name} 님"
        binding.myInfoNamePrintText.text = userInfo.name
        binding.myInfoIdPrintText.text = userInfo.ID
        binding.myInfoPhoneNumberPrintText.text = userInfo.phoneNumber
        binding.myInfoCareTargetNamePrintText.text = userInfo.careTargetName
        binding.myInfoCareTargetPhoneNumberPrintText.text = userInfo.careTargetPhoneNumber

        binding.myInfoNameEditText.setText(userInfo.name)
        binding.myInfoPhoneNumberEditText.setText(userInfo.phoneNumber)
        binding.myInfoCareTargetNameEditText.setText(userInfo.careTargetName)
        binding.myInfoCareTargetPhoneNumberEditText.setText(userInfo.careTargetPhoneNumber)

        memoList.clear()
        memoList.addAll(userInfo.guardianMemos?.map { m -> GuardianMemo(m.content) } ?: emptyList())
        guardianMemoAdapter.notifyDataSetChanged()
    }

    private fun setEditMode(isEditing: Boolean) {
        binding.myInfoNamePrintText.visibility = if (isEditing) View.GONE else View.VISIBLE
        binding.myInfoNameEditText.visibility = if (isEditing) View.VISIBLE else View.GONE

        binding.myInfoPhoneNumberPrintText.visibility = if (isEditing) View.GONE else View.VISIBLE
        binding.myInfoPhoneNumberEditText.visibility = if (isEditing) View.VISIBLE else View.GONE

        binding.myInfoCareTargetNamePrintText.visibility = if (isEditing) View.GONE else View.VISIBLE
        binding.myInfoCareTargetNameEditText.visibility = if (isEditing) View.VISIBLE else View.GONE

        binding.myInfoCareTargetPhoneNumberPrintText.visibility = if (isEditing) View.GONE else View.VISIBLE
        binding.myInfoCareTargetPhoneNumberEditText.visibility = if (isEditing) View.VISIBLE else View.GONE

        binding.editButton.visibility = if (isEditing) View.GONE else View.VISIBLE
        binding.saveButton.visibility = if (isEditing) View.VISIBLE else View.GONE
    }

    private fun updateUserInfo(userId: Int) {
        val name = binding.myInfoNameEditText.text.toString()
        val phoneNumber = binding.myInfoPhoneNumberEditText.text.toString()
        val careTargetName = binding.myInfoCareTargetNameEditText.text.toString()
        val careTargetPhoneNumber = binding.myInfoCareTargetPhoneNumberEditText.text.toString()

        val request = UpdateGuardianRequest(
            name = name,
            phoneNumber = phoneNumber,
            careTargetName = careTargetName,
            careTargetPhoneNumber = careTargetPhoneNumber
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.updateUserInfo(userId, request)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "정보가 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    loadUserInfo(userId) // Refresh user info
                    setEditMode(false)
                } else {
                    Toast.makeText(requireContext(), "정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}