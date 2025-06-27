package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillcare_capstone.adapter.GuardianMemoAdapter
import com.example.pillcare_capstone.data_class.GuardianMemo
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
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.getUserInfo(userId)
                    Log.d("DEBUG", "code = ${response.code()}")
                    Log.d("DEBUG", "body = ${Gson().toJson(response.body())}")
                    Log.d("DEBUG", "error = ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        val userInfo = response.body()
                        userInfo?.let {
                            binding.myInfoCompatButton.text = "${it.name} 님"
                            binding.myInfoNamePrintText.text = it.name
                            binding.myInfoIdPrintText.text = it.ID
                            binding.myInfoPhoneNumberPrintText.text = it.phoneNumber
                            binding.myInfoCareTargetNamePrintText.text = it.careTargetName
                            binding.myInfoCareTargetPhoneNumberPrintText.text = it.careTargetPhoneNumber

                            // 데이터 업데이트
                            memoList.clear()
                            memoList.addAll(it.guardianMemos?.map { m -> GuardianMemo(m.content) } ?: emptyList())
                            guardianMemoAdapter.notifyDataSetChanged()
                        }
                    } else {
                        DialogUtils.showAlertDialog(
                            requireContext(),
                            "알림",
                            "서버 오류: 사용자 정보를 가져올 수 없습니다."
                        )
                    }
                } catch (e: Exception) {
                    DialogUtils.showAlertDialog(
                        requireContext(),
                        "알림",
                        "네트워크 오류: ${e.message}"
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}