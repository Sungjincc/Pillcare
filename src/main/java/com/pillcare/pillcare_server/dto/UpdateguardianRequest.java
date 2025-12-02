package com.pillcare.pillcare_server.dto;

import java.util.List;

public class UpdateguardianRequest {
    private String name;
    private String phoneNumber;
    private List<UpdateGuardianMemoRequest> guardianMemos;

    public UpdateguardianRequest() {}

    public UpdateguardianRequest(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UpdateGuardianMemoRequest> getGuardianMemos() {
        return guardianMemos;
    }

    public void setGuardianMemos(List<UpdateGuardianMemoRequest> guardianMemos) {
        this.guardianMemos = guardianMemos;
    }
}
