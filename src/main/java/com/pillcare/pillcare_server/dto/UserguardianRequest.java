package com.pillcare.pillcare_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class UserguardianRequest {
    private String name;

    @NotNull(message = "ID is required")
    private String ID;

    @JsonProperty("phoneNumber")
    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "password is required")
    private String password;

    @NotNull(message = "careTarget is required")
    private String careTargetName;

    @NotNull(message = "caretargetphonenumber is required")
    private String careTargetPhoneNumber;

    public UserguardianRequest(String name, String ID, String phoneNumber, String password, String careTargetName, String careTargetPhoneNumber) {
        this.name = name;
        this.ID = ID;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.careTargetName = careTargetName;
        this.careTargetPhoneNumber = careTargetPhoneNumber;
    }

    public String getName() { return name; }
    public String getID() { return ID; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }
    public String getCareTargetName() { return careTargetName; }
    public String getCareTargetPhoneNumber() { return careTargetPhoneNumber; }
}
