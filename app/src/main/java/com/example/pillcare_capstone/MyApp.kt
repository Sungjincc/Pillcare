package com.example.pillcare_capstone

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakaotalk_native_app_key))
    }
}
