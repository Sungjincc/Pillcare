package com.example.pillcare_capstone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pillcare_capstone.databinding.ActivityMainBinding
import com.example.pillcare_capstone.fragment.*

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
                    viewBinding.toolbarFragmentName.text = "내 정보"
                    true
                }
                R.id.fragment_home -> {
                    showFragment(homeFragment)
                    viewBinding.toolbarFragmentName.text = "홈"
                    true
                }
                R.id.fragment_setting -> {
                    showFragment(settingFragment)
                    viewBinding.toolbarFragmentName.text = "설정"
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
                viewBinding.bottomNavigationView.setBackgroundResource(R.drawable.bg_bottom_nav_home)
            }
            else -> {
                viewBinding.fabHome.visibility = View.GONE
                viewBinding.bottomNavigationView.setBackgroundResource(R.drawable.bg_bottom_nav_default)
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
}