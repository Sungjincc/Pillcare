#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "config.h"

#define SERVER_URL_GET "http://192.168.243.49:8080/api/medicationAlert"
#define SERVER_URL_POST "http://192.168.243.49/api/medicationAlert"

const char* waterDispenserIP = "192.168.243.101";

String selectedBoxes = "";
bool alerting = false;
unsigned long lastBlinkTime = 0;
bool ledState = false;
int blinkInterval = 500;

bool redSent = false;
bool yellowSent = false;
bool greenSent = false;

bool lastRedState = LOW;
bool lastYellowState = LOW;
bool lastGreenState = LOW;

int multiIndex = 0;
unsigned long lastFetchTime = 0;
const unsigned long fetchInterval = 60000;  // 60초마다 받아온다.

String activeColors[3];
int activeCount = 0;

void connectToWiFi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Wi-Fi 연결 중...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500); Serial.print(".");
  }
  Serial.println("\n✅ Wi-Fi 연결 완료 " + WiFi.localIP().toString());
}

String fetchSelectedBoxesFromServer() {
  if (WiFi.status() == WL_CONNECTED) {
    WiFiClient client;
    HTTPClient http;

    String url = String(SERVER_URL_GET) + "/" + String(USER_ID);
    Serial.println("🌐 요청 URL: " + url);
    http.begin(client, url);
    int code = http.GET();

    if (code == HTTP_CODE_OK) {
      String payload = http.getString();
      Serial.println("📥 서버에서 받은 약통: " + payload);
      http.end();
      return payload;
    } else {
      Serial.println("⚠️ 서버 응답 실패: " + http.errorToString(code));
      http.end();
    }
  }
  return "";
}

void postMedicationTaken(const String& boxColor) {
  if (WiFi.status() == WL_CONNECTED) {
    WiFiClient client;
    HTTPClient http;

    http.begin(client, SERVER_URL_POST);
    http.addHeader("Content-Type", "application/json");

    String jsonBody = "{\"userId\":" + String(USER_ID) + ",\"taken\":\"" + boxColor + "\"}";
    int code = http.POST(jsonBody);

    if (code > 0) {
      Serial.println("📤 복용 전송 완료: " + jsonBody);
    } else {
      Serial.println("⚠️ 복용 전송 실패: " + http.errorToString(code));
    }
    http.end();
  }
}

void sendToWaterDispenser(const String& action) {
  if (WiFi.status() == WL_CONNECTED) {
    WiFiClient client;
    HTTPClient http;

    String url = "http://" + String(waterDispenserIP) + "/" + action;
    Serial.println("🚰 물 조리개 요청 URL: " + url);
    http.begin(client, url);
    int httpCode = http.GET();
    if (httpCode > 0) {
      Serial.println("✅ 응답 완료: HTTP " + String(httpCode));
    } else {
      Serial.println("❌ 응답 실패: " + http.errorToString(httpCode));
    }
    http.end();
  }
}

void updateActiveColors() {
  activeCount = 0;
  if (selectedBoxes.indexOf("red") != -1)    activeColors[activeCount++] = "red";
  if (selectedBoxes.indexOf("yellow") != -1) activeColors[activeCount++] = "yellow";
  if (selectedBoxes.indexOf("green") != -1)  activeColors[activeCount++] = "green";
  multiIndex = 0;
}

void setup() {
  Serial.begin(115200);
  delay(3000);

  pinMode(SENSOR_RED_PIN, INPUT);
  pinMode(SENSOR_YELLOW_PIN, INPUT);
  pinMode(SENSOR_GREEN_PIN, INPUT);
  pinMode(LED_RED_PIN, OUTPUT);
  pinMode(LED_GREEN_PIN, OUTPUT);
  pinMode(LED_BLUE_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);

  digitalWrite(LED_RED_PIN, HIGH);
  digitalWrite(LED_GREEN_PIN, HIGH);
  digitalWrite(LED_BLUE_PIN, HIGH);
  digitalWrite(BUZZER_PIN, LOW);

  connectToWiFi();
}

void loop() {
  if (!alerting && millis() - lastFetchTime >= fetchInterval) {
    lastFetchTime = millis();
    String newBoxes = fetchSelectedBoxesFromServer();
    if (newBoxes != "") {
      selectedBoxes = newBoxes;
      startAlert();
    }
  }

  if (alerting) {
    blinkAlert();

    bool currentRed = digitalRead(SENSOR_RED_PIN);
    bool currentYellow = digitalRead(SENSOR_YELLOW_PIN);
    bool currentGreen = digitalRead(SENSOR_GREEN_PIN);

    if (selectedBoxes.indexOf("red") != -1 && lastRedState == LOW && currentRed == HIGH && !redSent) {
      removeBoxColor("red");
      postMedicationTaken("red");
      redSent = true;
    }

    if (selectedBoxes.indexOf("yellow") != -1 && lastYellowState == LOW && currentYellow == HIGH && !yellowSent) {
      removeBoxColor("yellow");
      postMedicationTaken("yellow");
      yellowSent = true;
    }

    if (selectedBoxes.indexOf("green") != -1 && lastGreenState == LOW && currentGreen == HIGH && !greenSent) {
      removeBoxColor("green");
      postMedicationTaken("green");
      greenSent = true;
    }

    lastRedState = currentRed;
    lastYellowState = currentYellow;
    lastGreenState = currentGreen;

    if (activeCount == 0) {
      stopAlert();
    }
  }

  delay(10);
}

void startAlert() {
  Serial.println("🚨 복용 알림 시작: " + selectedBoxes);
  alerting = true;
  sendToWaterDispenser("alert");

  updateActiveColors();
  multiIndex = 0;
  ledState = true;
  lastBlinkTime = millis() - blinkInterval;

  digitalWrite(BUZZER_PIN, HIGH);
}

void stopAlert() {
  Serial.println("✅ 복용 완료 → 알림 종료");
  alerting = false;
  redSent = yellowSent = greenSent = false;

  digitalWrite(LED_RED_PIN, HIGH);
  digitalWrite(LED_GREEN_PIN, HIGH);
  digitalWrite(LED_BLUE_PIN, HIGH);
  digitalWrite(BUZZER_PIN, LOW);

  sendToWaterDispenser("reset");
}

void blinkAlert() {
  digitalWrite(LED_RED_PIN, HIGH);
  digitalWrite(LED_GREEN_PIN, HIGH);
  digitalWrite(LED_BLUE_PIN, HIGH);

  if (activeCount == 0) {
    stopAlert();
    return;
  }

  if (multiIndex >= activeCount) multiIndex = 0;

  if (ledState) {
    String current = activeColors[multiIndex];

    if (current == "red") {
      digitalWrite(LED_RED_PIN, LOW);
    } else if (current == "yellow") {
      digitalWrite(LED_RED_PIN, LOW);
      digitalWrite(LED_GREEN_PIN, LOW);
    } else if (current == "green") {
      digitalWrite(LED_GREEN_PIN, LOW);
    }
  }

  if (millis() - lastBlinkTime >= blinkInterval) {
    lastBlinkTime = millis();
    ledState = !ledState;
    if (ledState) {
      multiIndex = (multiIndex + 1) % activeCount;
    }
  }
}

void removeBoxColor(String color) {
  selectedBoxes.replace(color + ",", "");
  selectedBoxes.replace("," + color, "");
  selectedBoxes.replace(color, "");
  updateActiveColors();
  multiIndex = 0;
}
