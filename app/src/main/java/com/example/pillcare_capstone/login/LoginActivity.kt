package com.example.pillcare_capstone.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pillcare_capstone.MainActivity
import com.example.pillcare_capstone.data_class.LoginRequest
import com.example.pillcare_capstone.data_class.LoginResponse
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.example.pillcare_capstone.network.RetrofitClient


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        val token = prefs.getString("token", null)

//        if (!token.isNullOrEmpty()) {
//            // 2. 토큰이 존재하면 자동 로그인 (서버 없이 앱 진입)
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//            return
//        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        // 회원가입
        binding.signupLayout.setOnClickListener {
            startActivity(Intent(this, SignUpActivityOne::class.java))
        }

        // 비밀번호 찾기
        binding.findPasswordTextView.setOnClickListener {
            startActivity(Intent(this, FindPasswordActivityOne::class.java))
        }

        // 일반 로그인
        binding.loginButton.setOnClickListener {
            val id = binding.idEditText.text.toString()
            val pw = binding.passwordEditText.text.toString()

            val request = LoginRequest(ID = id, password = pw)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d("LoginRequest", "아이디: ${request.ID}, 비밀번호: ${request.password}") // 요청 데이터 로그

                    val response: Response<LoginResponse> = RetrofitClient.apiService.login(request)

                    withContext(Dispatchers.Main) {
                        Log.d("LoginResponse", "응답 코드: ${response.code()}") // 응답 코드 로그
                        Log.d("LoginResponse", "응답 body: ${response.body()}") // 성공 응답 로그
                        Log.d("LoginResponse", "에러 body: ${response.errorBody()?.string()}")
                        if (response.isSuccessful) {
                            val body = response.body()

                            // 로그인 토큰 저장
                            val prefs = getSharedPreferences("user", MODE_PRIVATE)
                            prefs.edit()
                                .putString("token", body?.token)
                                .putInt("userId", body?.userId ?: -1)
                                .putString("name", body?.name ?: "")
                                .apply()

                            // 홈 화면 이동
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }else
                        {
                            binding.errorTextView.text = "아이디 또는 비밀번호가 올바르지 않습니다."
                            binding.errorTextView.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("LoginError", e.localizedMessage ?: "알 수 없는 오류")
                    }
                }
            }
        }

        // 네이버 로그인
        binding.naverButton.setOnClickListener { naverAuthenticate() }

        // 카카오 로그인
        binding.kakaoButton.setOnClickListener { kakaoLogin() }
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