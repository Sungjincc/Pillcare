package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillcare_capstone.adapter.MedicinePlusAdapter
import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val medicinePlusList = mutableListOf<MedicinePlus>()
    private lateinit var adapter: MedicinePlusAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

    }

    private fun initRecyclerView() {
        adapter = MedicinePlusAdapter(medicinePlusList, layoutInflater)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter
        binding.recyclerViewHome.setHasFixedSize(true)
        binding.recyclerViewHome.isNestedScrollingEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addNewMedicineItem() {
        medicinePlusList.add(MedicinePlus())
        adapter.notifyItemInserted(medicinePlusList.size - 1)
        binding.recyclerViewHome.scrollToPosition(medicinePlusList.size - 1)
    }



}