package com.pillcare.pillcare_server.entity;

import java.io.Serializable;

public class MedicineKey implements Serializable {
    private int userId;
    private String pillCaseColor;

    public MedicineKey() {}

    public MedicineKey(int userId, String pillCaseColor) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
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
}
