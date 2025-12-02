#ifndef CONFIG_H
#define CONFIG_H

// Wi-Fi 설정
#define WIFI_SSID     "Test"
#define WIFI_PASSWORD "20010829"

// 서버 URL (필요 시 수정)
#define SERVER_URL "http://192.168.85.247:8080/api/medicationAlert"

// 사용자 ID (서버 전송 시 구분용)
 #define USER_ID "110"

// 알림 지속 시간 (밀리초)
#define ALERT_DURATION 3000

// 자석 센서 핀 (약통 3개)
#define SENSOR_RED_PIN     D1   // 빨간 상자
#define SENSOR_YELLOW_PIN  D2   // 노란 상자
#define SENSOR_GREEN_PIN   D3   // 초록 상자

// RGB LED 핀 (CA 타입: 공통 애노드는 3.3V에 연결)
#define LED_RED_PIN    D6       // R
#define LED_GREEN_PIN  D7       // G
#define LED_BLUE_PIN   D8       // B

// 부저 핀
#define BUZZER_PIN     D5

#endif
