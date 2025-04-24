package com.example.pillcare_capstone.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pillcare_capstone.databinding.SettingFragmentBinding
import com.example.pillcare_capstone.login.LoginActivity
import com.example.pillcare_capstone.setting.*

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
        setupListeners()
    }

    private fun setupListeners()
    {
        binding.settingChangeMyInfo.setOnClickListener {
            val intent = Intent(requireActivity(), ChangeMyInfoActivity::class.java)
            startActivity(intent)
        }
        binding.settingChangeCareTarget.setOnClickListener{
            val intent = Intent(requireActivity(),ChangeCareTargetActivity::class.java)
            startActivity(intent)
        }
        binding.settingChangePassword.setOnClickListener{
            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.settingSetPushAlarm.setOnClickListener{
            val intent = Intent(requireActivity(),SetPushAlarmActivity::class.java)
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
            val intent = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}