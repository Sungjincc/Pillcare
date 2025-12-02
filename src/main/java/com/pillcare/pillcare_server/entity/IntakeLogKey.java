package com.pillcare.pillcare_server.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class IntakeLogKey implements Serializable {
    private int userId;
    private String pillCaseColor;
    private String alarmTime;
    private String dayOfWeek;
    private LocalDate scheduledDate;

    public IntakeLogKey() {}

    public IntakeLogKey(int userId, String pillCaseColor, String alarmTime, String dayOfWeek, LocalDate scheduledDate) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.alarmTime = alarmTime;
        this.dayOfWeek = dayOfWeek;
        this.scheduledDate = scheduledDate;
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

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
