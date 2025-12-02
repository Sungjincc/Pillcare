package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;

@Entity
@IdClass(MedicineTimePlusKey.class)
@Table(name = "medicine_time_plus")
public class MedicineTimePlus {

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
    @Column(name = "selected_days")
    private String selectedDays;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
        @JoinColumn(name = "pill_case_color", referencedColumnName = "pill_case_color", insertable = false, updatable = false)
    })
    private MedicinePlus medicinePlus;

    public MedicineTimePlus() {
    }

    public MedicineTimePlus(int userId, String pillCaseColor, String alarmTime, String selectedDays, MedicinePlus medicinePlus) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.alarmTime = alarmTime;
        this.selectedDays = selectedDays;
        this.medicinePlus = medicinePlus;
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

    public MedicinePlus getMedicinePlus() {
        return medicinePlus;
    }

    public void setMedicinePlus(MedicinePlus medicinePlus) {
        this.medicinePlus = medicinePlus;
    }
}
