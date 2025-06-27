package com.example.pillcare_capstone.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pillcare_capstone.R
import com.example.pillcare_capstone.data_class.TokenRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "알림"
        val body = remoteMessage.notification?.body ?: "내용 없음"

        Log.d("FCM", "📲 받은 알림: $title / $body")

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "🆕 새 FCM 토큰: $token")

        val userId = getSharedPreferences("prefs", MODE_PRIVATE).getInt("userId", -1)

        if (userId != -1) {
            val request = TokenRequest(userId, token)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.apiService.sendFcmToken(request)
                    if (response.isSuccessful) {
                        Log.d("FCM", "✅ 토큰 서버에 성공적으로 전송됨")
                    } else {
                        Log.e("FCM", "❌ 서버 전송 실패: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("FCM", "❗ 네트워크 오류: ${e.message}")
                }
            }
        } else {
            Log.e("FCM", "❌ 유효하지 않은 userId")
        }
    }

    private fun showNotification(title: String, message: String) {
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

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.bg_pill_red)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}