package com.pillcare.pillcare_server.dto;

import java.util.List;

public class UpdateCareTargetRequest {
    private String careTargetName;
    private String careTargetPhoneNumber;

    private List<UpdateGuardianMemoRequest> guardianMemos;

    public UpdateCareTargetRequest() {}

    public UpdateCareTargetRequest(String careTargetName, String careTargetPhoneNumber) {
        this.careTargetName = careTargetName;
        this.careTargetPhoneNumber = careTargetPhoneNumber;
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

    public List<UpdateGuardianMemoRequest> getGuardianMemos() {
        return guardianMemos;
    }

    public void setGuardianMemos(List<UpdateGuardianMemoRequest> guardianMemos) {
        this.guardianMemos = guardianMemos;
    }
}
