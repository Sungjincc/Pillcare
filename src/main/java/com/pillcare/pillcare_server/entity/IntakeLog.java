package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@IdClass(IntakeLogKey.class)
@Table(name = "intake_log")
public class IntakeLog implements Serializable {

    @Id
    private int userId;

    @Id
    @Column(name = "pill_case_color")
    private String pillCaseColor;

    @Id
    private String alarmTime;

    @Id
    private String dayOfWeek;

    @Id
    private LocalDate scheduledDate;

    private LocalDateTime takenTime;

    private boolean taken = false;

    @Enumerated(EnumType.STRING)
    private PillboxStatus status = PillboxStatus.CLOSED;

    public IntakeLog() {}

    public IntakeLog(int userId, String pillCaseColor, String alarmTime, String dayOfWeek, LocalDate scheduledDate, boolean taken, LocalDateTime takenTime, PillboxStatus status) {
		this.userId = userId;
		this.pillCaseColor = pillCaseColor;
		this.alarmTime = alarmTime;
		this.dayOfWeek = dayOfWeek;
		this.scheduledDate = scheduledDate;
		this.taken = taken;
		this.takenTime = takenTime;
		this.status = status;
	}


    // Getters and Setters

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

    public LocalDateTime getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(LocalDateTime takenTime) {
        this.takenTime = takenTime;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public PillboxStatus getStatus() {
        return status;
    }

    public void setStatus(PillboxStatus status) {
        this.status = status;
    }

    // equals and hashCode for composite key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntakeLog)) return false;
        IntakeLog that = (IntakeLog) o;
        return userId == that.userId &&
                Objects.equals(pillCaseColor, that.pillCaseColor) &&
                Objects.equals(alarmTime, that.alarmTime) &&
                Objects.equals(dayOfWeek, that.dayOfWeek) &&
                Objects.equals(scheduledDate, that.scheduledDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, pillCaseColor, alarmTime, dayOfWeek, scheduledDate);
    }
}
