package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "medicine_schedule")
@IdClass(MedicineScheduleKey.class)
public class MedicineSchedule {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "pill_case_color")
    private String pillCaseColor;

    @Id
    @Column(name = "alarm_time")
    private String alarmTime;

    @Id
    @Column(name = "day_of_week")
    private String dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
        @JoinColumn(name = "pill_case_color", referencedColumnName = "pill_case_color", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Medicine medicine;

    // 기본 생성자
    public MedicineSchedule() {}

    // 모든 필드 생성자
    public MedicineSchedule(int userId, String pillCaseColor, String alarmTime, String dayOfWeek, Medicine medicine) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.alarmTime = alarmTime;
        this.dayOfWeek = dayOfWeek;
        this.medicine = medicine;
    }

    // Getter/Setter
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

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
}
