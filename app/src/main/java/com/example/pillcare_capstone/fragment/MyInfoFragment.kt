package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.databinding.MyInfoFragmentBinding

class MyInfoFragment : Fragment() {
    private var _binding: MyInfoFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}