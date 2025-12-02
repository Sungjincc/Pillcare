package com.pillcare.pillcare_server.model;

import java.util.List;

public class Userguardian {
    private int userId = 0;
    private String name;
    private String ID;
    private String phoneNumber;
    private String password;
    private String careTargetName;
    private String careTargetPhoneNumber;
    private List<GuardianMemo> guardianMemos;

    public Userguardian() {
    }

    public Userguardian(int userId, String name, String ID, String phoneNumber,
                        String password, String careTargetName, String careTargetPhoneNumber,
                        List<GuardianMemo> guardianMemos) {
        this.userId = userId;
        this.name = name;
        this.ID = ID;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.careTargetName = careTargetName;
        this.careTargetPhoneNumber = careTargetPhoneNumber;
        this.guardianMemos = guardianMemos;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCareTargetName() {
        return careTargetName;
    }

    public void setCareTargetName(String careTargetName) {
        this.careTargetName = careTargetName;
    }

    public String getCareTargetPhoneNumber() {
        return careTargetPhoneNumber;
    }

    public void setCareTargetPhoneNumber(String careTargetPhoneNumber) {
        this.careTargetPhoneNumber = careTargetPhoneNumber;
    }

    public List<GuardianMemo> getGuardianMemos() {
        return guardianMemos;
    }

    public void setGuardianMemos(List<GuardianMemo> guardianMemos) {
        this.guardianMemos = guardianMemos;
    }
}
