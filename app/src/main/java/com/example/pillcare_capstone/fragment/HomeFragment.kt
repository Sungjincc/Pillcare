package com.example.pillcare_capstone.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
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
import com.example.pillcare_capstone.utils.toRequest
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val medicinePlusList = mutableListOf<MedicinePlus>()
    private lateinit var adapter: MedicinePlusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        if (userId == -1) {
            Log.e("HomeFragment", "userId가 유효하지 않음")
            return
        }

        // 서버에서 데이터 가져오고 RecyclerView 초기화
        CoroutineScope(Dispatchers.IO).launch {
            val data = fetchMedicineSchedules(userId)
            withContext(Dispatchers.Main) {
                setupRecyclerView(userId, data)
            }
        }
    }

    private fun setupRecyclerView(userId: Int, data: List<MedicinePlus>) {
        medicinePlusList.clear()
        medicinePlusList.addAll(data)

        adapter = MedicinePlusAdapter(medicinePlusList, layoutInflater, userId)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter
        binding.recyclerViewHome.setHasFixedSize(true)
        binding.recyclerViewHome.isNestedScrollingEnabled = false
    }

    private suspend fun fetchMedicineSchedules(userId: Int): List<MedicinePlus> {
        return try {
            val response = RetrofitClient.apiService.getMedicineSchedules(userId)
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("약 조회 응답", com.google.gson.Gson().toJson(body))

                body?.medicineList?.map { res ->
                    val first = res.schedules.firstOrNull()
                    val rest = res.schedules.drop(1)

                    MedicinePlus(
                        medicineName = res.medicineName,
                        alarmTime = first?.time ?: "",
                        selectedDays = first?.daysOfWeek?.toMutableList() ?: mutableListOf(),
                        timeList = rest.map {
                            MedicineTimePlus(it.time, it.daysOfWeek.toMutableList())
                        }.toMutableList(),
                        pillCaseColor = PillCaseColor.valueOf(res.pillCaseColor.uppercase())
                    )
                } ?: emptyList()
            } else {
                Log.e("조회 실패", response.errorBody()?.string() ?: "알 수 없음")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("조회 예외", e.localizedMessage ?: "예외 발생")
            emptyList()
        }
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
            // 🔹 약 디폴트값 설정
            newItem.medicineName = ""
            newItem.alarmTime = "12:00"
            newItem.isPosted = false

            val prefs = requireContext().getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
            val userId = prefs.getInt("userId", -1)

            if (userId != -1) {
                val request = newItem.toRequest(userId)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitClient.apiService.sendSchedule(request)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                newItem.isPosted = true
                                medicinePlusList.add(newItem)
                                adapter.notifyItemInserted(medicinePlusList.size - 1)
                                binding.recyclerViewHome.scrollToPosition(medicinePlusList.size - 1)
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.e("약 기본 등록 실패", "${response.code()}: $errorBody")
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Log.e("약 생성 예외", e.localizedMessage ?: "예외 발생")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}