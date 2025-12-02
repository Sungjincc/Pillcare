package com.pillcare.pillcare_server.entity;

import java.io.Serializable;
import java.util.Objects;

public class MedicinePlusKey implements Serializable {
    private int userId;
    private String pillCaseColor;

    public MedicinePlusKey() {
    }

    public MedicinePlusKey(int userId, String pillCaseColor) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicinePlusKey)) return false;
        MedicinePlusKey that = (MedicinePlusKey) o;
        return userId == that.userId &&
                Objects.equals(pillCaseColor, that.pillCaseColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, pillCaseColor);
    }
}
