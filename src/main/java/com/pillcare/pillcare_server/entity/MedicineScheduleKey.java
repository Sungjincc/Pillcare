package com.pillcare.pillcare_server.entity;

import java.io.Serializable;

public class MedicineScheduleKey implements Serializable {
    private int userId;
    private String pillCaseColor;
    private String alarmTime;
    private String dayOfWeek;

    public MedicineScheduleKey() {}

    public MedicineScheduleKey(int userId, String pillCaseColor, String alarmTime, String dayOfWeek) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.alarmTime = alarmTime;
        this.dayOfWeek = dayOfWeek;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPillCaseColor() {
        return pillCaseColor;
    }

    public void setPillCaseColor(String pillCaseColor) {
        this.pillCaseColor = pillCaseColor;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
