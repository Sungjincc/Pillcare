package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillcare_capstone.adapter.MedicinePlusAdapter
import com.example.pillcare_capstone.data_class.MedicinePlus
import com.example.pillcare_capstone.data_class.MedicineTimePlus
import com.example.pillcare_capstone.data_class.PillCaseColor
import com.example.pillcare_capstone.databinding.HomeFragmentBinding
import com.example.pillcare_capstone.network.RetrofitClient
import com.example.pillcare_capstone.utils.DialogUtils
import com.example.pillcare_capstone.utils.PillCaseColorSelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val prefs = requireContext().getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        fetchMedicineSchedules(userId) // ✅ 여기서 불러오기
    }

    private fun initRecyclerView() {
        adapter = MedicinePlusAdapter(medicinePlusList, layoutInflater, -1) // 초기에는 userId 없음
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter
        binding.recyclerViewHome.setHasFixedSize(true)
        binding.recyclerViewHome.isNestedScrollingEnabled = false
    }

    private fun fetchMedicineSchedules(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getMedicineSchedules(userId)
                if (response.isSuccessful) {
                    val medicineList = response.body() ?: emptyList()

                    val convertedList = medicineList.map { res ->
                        MedicinePlus(
                            medicineName = res.medicineName,
                            alarmTime = res.schedules.firstOrNull()?.time ?: "",
                            selectedDays = res.schedules.firstOrNull()?.daysOfWeek?.toMutableList() ?: mutableListOf(),
                            timeList = res.schedules.map {
                                MedicineTimePlus(it.time, it.daysOfWeek.toMutableList())
                            }.toMutableList(),
                            pillCaseColor = PillCaseColor.valueOf(res.pillCaseColor.uppercase())
                        )
                    }.toMutableList()

                    withContext(Dispatchers.Main) {
                        medicinePlusList.clear()
                        medicinePlusList.addAll(convertedList)
                        adapter = MedicinePlusAdapter(medicinePlusList, layoutInflater, userId)
                        binding.recyclerViewHome.adapter = adapter
                    }

                } else {
                    Log.e("조회 실패", response.errorBody()?.string() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("조회 예외", e.localizedMessage ?: "예외 발생")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addNewMedicineItem() {
        if (medicinePlusList.size >= 3) {
            DialogUtils.showLimitReachedDialog(requireContext())
            return
        }

        val usedColors = medicinePlusList.mapNotNull { it.pillCaseColor }.toSet()
        PillCaseColorSelector.showColorSelectionDialog(
            context = requireContext(),
            usedColors = usedColors
        ) { newItem ->
            medicinePlusList.add(newItem)
            adapter.notifyItemInserted(medicinePlusList.size - 1)
            binding.recyclerViewHome.scrollToPosition(medicinePlusList.size - 1)
        }
    }
}