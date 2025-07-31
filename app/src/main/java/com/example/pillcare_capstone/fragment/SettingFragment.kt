package com.example.pillcare_capstone.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pillcare_capstone.databinding.SettingFragmentBinding
import com.example.pillcare_capstone.login.LoginActivity
import com.example.pillcare_capstone.setting.*
import com.example.pillcare_capstone.setting.change_care_target.ChangeCareTargetActivity
import com.example.pillcare_capstone.setting.change_my_info.ChangeMyInfoActivity
import com.example.pillcare_capstone.setting.change_password.ChangePasswordActivity
import com.example.pillcare_capstone.setting.set_push_alarm.SetPushAlarmActivity


class SettingFragment : Fragment() {

    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)
        Log.d("userId", "$userId")
        setupListeners(userId)
    }

    private fun setupListeners(userId: Int)
    {
        binding.settingChangeMyInfo.setOnClickListener {
            val intent = Intent(requireActivity(), ChangeMyInfoActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.settingChangeCareTarget.setOnClickListener{
            val intent = Intent(requireActivity(), ChangeCareTargetActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.settingChangePassword.setOnClickListener{
            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.settingSetPushAlarm.setOnClickListener{
            val intent = Intent(requireActivity(), SetPushAlarmActivity::class.java)
            startActivity(intent)
        }
        binding.settingVersionOfApp.setOnClickListener {
            val intent = Intent(requireActivity(),VersionOfAppActivity::class.java)
            startActivity(intent)
        }
        binding.settingLicense.setOnClickListener {
            val intent = Intent(requireActivity(),LicenseActivity::class.java)
            startActivity(intent)
        }
        binding.settingLogoutEfab.setOnClickListener{
            showLogoutConfirmDialog()
        }
    }

    private fun showLogoutConfirmDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("로그아웃")
            setMessage("정말로 로그아웃 하시겠습니까?")
            setPositiveButton("로그아웃") { _, _ ->
                performLogout()
            }
            setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.create().show()
    }

    private fun performLogout() {
        // SharedPreferences에서 사용자 정보 제거
        val prefs = requireContext().getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        
        // 로그인 화면으로 이동하고 백스택 제거
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}