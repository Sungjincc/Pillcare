package com.pillcare.pillcare_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pillcare.pillcare_server.dto.GuardianMemoDto;
import java.util.List;

public class UserguardianResponse {
    private int userId;
    private String name;
    private String phoneNumber;

    @JsonProperty("ID")
    private String ID;
    private String careTargetName;
    private String careTargetPhoneNumber;
    private List<GuardianMemoDto> guardianMemos;

    public UserguardianResponse(
        int userId,
        String name,
        String phoneNumber,
        String ID,
        String careTargetName,
        String careTargetPhoneNumber,
        List<GuardianMemoDto> guardianMemos
    ) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.ID = ID;
        this.careTargetName = careTargetName;
        this.careTargetPhoneNumber = careTargetPhoneNumber;
        this.guardianMemos = guardianMemos;
    }

    // Getter methods ...
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getID() { return ID; }
    public String getCareTargetName() { return careTargetName; }
    public String getCareTargetPhoneNumber() { return careTargetPhoneNumber; }
    public List<GuardianMemoDto> getGuardianMemos() { return guardianMemos; }
}
