#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <Servo.h>

#define SERVO_PIN D4
#define SERVO_BLOCK_ANGLE 180
#define SERVO_OPEN_ANGLE 0  //

Servo waterServo;

const char* ssid = "Test";
const char* password = "20010829";

ESP8266WebServer server(80);

void setup() {
  Serial.begin(115200);
  delay(5000);

  waterServo.attach(SERVO_PIN);
  waterServo.write(SERVO_OPEN_ANGLE);  // ✅ 시작 시 열림 위치

  WiFi.begin(ssid, password);
  Serial.print("Wi-Fi 연결 중...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500); Serial.print(".");
  }
  Serial.println("\n✅ 연결됨! IP: " + WiFi.localIP().toString());

  server.on("/alert", HTTP_GET, []() {
    Serial.println("💧 알림 수신 → 조리개 닫기");
    waterServo.write(SERVO_BLOCK_ANGLE);
    server.send(200, "text/plain", "Servo closed");
  });

  server.on("/reset", HTTP_GET, []() {
    Serial.println("🔄 리셋 수신 → 조리개 열기");
    waterServo.write(SERVO_OPEN_ANGLE);
    server.send(200, "text/plain", "Servo opened");
  });

  server.begin();
  Serial.println("-----서버 시작-----");
}

void loop() {
  server.handleClient();
}
