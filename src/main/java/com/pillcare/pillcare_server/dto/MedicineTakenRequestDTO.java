package com.pillcare.pillcare_server.dto;

public class MedicineTakenRequestDTO {
    private int userId;
    private String pillCaseColor;

    public MedicineTakenRequestDTO(int userId, String pillCaseColor) {
        this.userId = userId;
        this.pillCaseColor = pillCaseColor;
    }

    public int getUserId() {
        return userId;
    }

    public String getPillCaseColor() {
        return pillCaseColor;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPillCaseColor(String pillCaseColor) {
        this.pillCaseColor = pillCaseColor;
    }
}
