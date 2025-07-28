package com.example.pillcare_capstone

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pillcare_capstone.data_class.TokenRequest
import com.example.pillcare_capstone.databinding.ActivityMainBinding
import com.example.pillcare_capstone.fragment.*
import com.example.pillcare_capstone.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    // 프래그먼트 인스턴스
    private val homeFragment = HomeFragment()
    private val myInfoFragment = MyInfoFragment()
    private val settingFragment = SettingFragment()

    private var activeFragment: Fragment = myInfoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        val userName = prefs.getString("name", "")

        // 이름이 존재하면 툴바에 표시
        if (!userName.isNullOrEmpty()) {
            viewBinding.toolbarCareTargetNameText.text = "$userName 님"
        }

        setupFragments()
        setupBottomNavigation()
        initListeners()

        // 앱 시작 시 홈 프래그먼트를 보여줌
        if (savedInstanceState == null) {
            showFragment(homeFragment)
            viewBinding.bottomNavigationView.selectedItemId = R.id.fragment_home
        }
    }

    // 초기 프래그먼트들을 FragmentManager에 추가
    private fun setupFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.mainContainer, settingFragment).hide(settingFragment)
            add(R.id.mainContainer, myInfoFragment).hide(myInfoFragment)
            add(R.id.mainContainer, homeFragment)
        }.commit()
    }

    // 바텀 네비게이션 이벤트
    private fun setupBottomNavigation() {
        viewBinding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_myinfo -> {
                    showFragment(myInfoFragment)
                    true
                }
                R.id.fragment_home -> {
                    showFragment(homeFragment)
                    true
                }
                R.id.fragment_setting -> {
                    showFragment(settingFragment)
                    true
                }
                else -> false
            }
        }

    }

    // FloatingActionButton 클릭 리스너
    private fun initListeners() {
        viewBinding.fabHome.setOnClickListener {
            if (activeFragment is HomeFragment) {
                (activeFragment as HomeFragment).addNewMedicineItem()
            }
        }
        viewBinding.toolbarAlarmImageButton.setOnClickListener {
            triggerTestNotification()
        }
    }

    // 프래그먼트 전환
    private fun showFragment(fragment: Fragment) {
        if (fragment == activeFragment) return

        supportFragmentManager.beginTransaction().apply {
            hide(activeFragment)
            show(fragment)
        }.commit()

        activeFragment = fragment
        updateBottomNavForFragment(fragment)
        updateToolbarView(fragment)

    }

    // FAB 및 BottomNavigationView 배경 상태 변경
    private fun updateBottomNavForFragment(fragment: Fragment) {
        when (fragment) {
            is HomeFragment -> {
                viewBinding.fabHome.visibility = View.VISIBLE
            }
            else -> {
                viewBinding.fabHome.visibility = View.GONE
            }
        }
    }

    // 툴바 상태 업데이트
    private fun updateToolbarView(fragment: Fragment) {
        when (fragment) {
            is HomeFragment -> {
                viewBinding.toolbarCareTargetImage.visibility = View.VISIBLE
                viewBinding.toolbarCareTargetNameText.visibility = View.VISIBLE
            }
            else -> {
                viewBinding.toolbarCareTargetImage.visibility = View.INVISIBLE
                viewBinding.toolbarCareTargetNameText.visibility = View.INVISIBLE
            }
        }
    }

    private fun sendTokenToServer(userId: Int, token: String) {
        val request = TokenRequest(userId, token)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.sendFcmToken(request)
                if (response.isSuccessful) {
                    Log.d("FCM", "✅ FCM 토큰 서버 전송 성공")
                } else {
                    Log.e("FCM", " 서버 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FCM", " FCM 토큰 전송 중 오류 발생", e)
            }
        }
    }

    private fun triggerTestNotification() {
        val title = "💊미복용 알림"
        val body = "초록색약통 : 타이레놀을 복용하지 않았습니다."

        val channelId = "pill_reminder_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "약 복용 알림 채널",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "약 복용 여부를 사용자에게 알려줍니다"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.bg_pill_red)  // 네가 쓰는 알림 아이콘
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(0, builder.build())
    }
}
