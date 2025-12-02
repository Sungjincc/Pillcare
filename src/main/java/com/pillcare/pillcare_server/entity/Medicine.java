package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "medicine")
@IdClass(MedicineKey.class)
public class Medicine {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "pill_case_color")
    private String pillCaseColor;

    @Column(name = "medicine_name")
    private String medicineName;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineSchedule> scheduleList;

    public Medicine() {}
    
    public Medicine(int userId, String medicineName, String pillCaseColor) {
        this.userId = userId;
        this.medicineName = medicineName;
        this.pillCaseColor = pillCaseColor;
    }


    public Medicine(int userId, String pillCaseColor, String medicineName, List<MedicineSchedule> scheduleList) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.medicineName = medicineName;
        this.scheduleList = scheduleList;
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

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public List<MedicineSchedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<MedicineSchedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @Override
    public String toString() {
        return "Medicine(userId=" + userId + ", pillCaseColor='" + pillCaseColor + "', medicineName='" + medicineName + "')";
    }
}
