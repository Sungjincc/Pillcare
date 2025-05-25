package com.example.pillcare_capstone.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.MainActivity
import com.example.pillcare_capstone.databinding.ActivityLoginBinding
import com.example.pillcare_capstone.find_password.FindPasswordActivityOne
import com.example.pillcare_capstone.sign_up.SignUpActivityOne
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initListeners()
    }

    private fun initListeners() {
        // 회원가입 화면으로 이동
        binding.signupLayout.setOnClickListener {
            startActivity(Intent(this, SignUpActivityOne::class.java))
        }

        // 비밀번호 찾기 화면으로 이동
        binding.findPasswordTextView.setOnClickListener {
            startActivity(Intent(this, FindPasswordActivityOne::class.java))
        }

        // 일반 로그인
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // 네이버 로그인
        binding.naverButton.setOnClickListener {
            naverAuthenticate()
        }

        // 카카오 로그인
        binding.kakaoButton.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun naverAuthenticate() {
        NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
            override fun onSuccess() {
                val accessToken = NaverIdLoginSDK.getAccessToken()
                getUserInfo(accessToken)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("NaverLogin", "Failure: $message")
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e("NaverLogin", "Error: $message")
            }
        })
    }

    private fun getUserInfo(token: String?) {
        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                val profile = result.profile
                Log.d("NaverLogin", "User Name: ${profile?.name}, Email: ${profile?.email}")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("NaverLogin", "UserInfo Failure: $message")
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e("NaverLogin", "UserInfo Error: $message")
            }
        })
    }

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("카카오 로그인", "실패: ${error.message}")
            } else if (token != null) {
                Log.i("카카오 로그인", "성공: ${token.accessToken}")
                getUserInfoKaKao()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun getUserInfoKaKao() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("카카오 사용자 정보", "요청 실패: ${error.message}")
            } else if (user != null) {
                Log.i("카카오 사용자 정보", "닉네임: ${user.kakaoAccount?.profile?.nickname}")
            }
        }
    }
}