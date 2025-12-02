package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@IdClass(MedicinePlusKey.class)
@Table(name = "medicine_plus")
public class MedicinePlus {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "pill_case_color")
    private String pillCaseColor;

    @Column(name = "medicine_name")
    private String medicineName;

    @OneToMany(mappedBy = "medicinePlus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicineTimePlus> timeConfigs = new ArrayList<>();

    public MedicinePlus() {
    }

    public MedicinePlus(int userId, String pillCaseColor, String medicineName, List<MedicineTimePlus> timeConfigs) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
        this.medicineName = medicineName;
        this.timeConfigs = timeConfigs;
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

    public List<MedicineTimePlus> getTimeConfigs() {
        return timeConfigs;
    }

    public void setTimeConfigs(List<MedicineTimePlus> timeConfigs) {
        this.timeConfigs = timeConfigs;
    }
}
