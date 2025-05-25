package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillcare_capstone.R
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
        if (medicinePlusList.size >= 3) {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("약통 부족")
                .setMessage("약은 최대 3개까지만 등록할 수 있습니다.")
                .setPositiveButton("확인", null)
                .create()

            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(
                    requireContext().let { context ->
                        androidx.core.content.ContextCompat.getColor(context, R.color.textMainColor)
                    }
                )
            }

            dialog.show()
            return
        }
        medicinePlusList.add(MedicinePlus())
        adapter.notifyItemInserted(medicinePlusList.size - 1)
        binding.recyclerViewHome.scrollToPosition(medicinePlusList.size - 1)
    }



}