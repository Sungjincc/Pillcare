package com.pillcare.pillcare_server.entity;

import java.io.Serializable;
import java.util.Objects;

public class MedicineTimePlusKey implements Serializable {
    private int userId;
    private String pillCaseColor;
    private String alarmTime;
    private String selectedDays;

    public MedicineTimePlusKey() {
    }

    public MedicineTimePlusKey(int userId, String pillCaseColor, String alarmTime, String selectedDays) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.alarmTime = alarmTime;
        this.selectedDays = selectedDays;
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

    public String getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String selectedDays) {
        this.selectedDays = selectedDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicineTimePlusKey)) return false;
        MedicineTimePlusKey that = (MedicineTimePlusKey) o;
        return userId == that.userId &&
                Objects.equals(pillCaseColor, that.pillCaseColor) &&
                Objects.equals(alarmTime, that.alarmTime) &&
                Objects.equals(selectedDays, that.selectedDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, pillCaseColor, alarmTime, selectedDays);
    }
}
