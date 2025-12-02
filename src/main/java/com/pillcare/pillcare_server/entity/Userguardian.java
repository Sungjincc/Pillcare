package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Userguardian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private int userId;

    private String name;

    @JsonProperty("phoneNumber")
    @NotNull(message = "Phone number is required")
    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonProperty("ID")
    @Column(name = "ID")
    private String ID;

    private String password;

    @Column(name = "care_target_name")
    private String careTargetName;

    @Column(name = "care_target_phone_number")
    private String careTargetPhoneNumber;

    @OneToMany(mappedBy = "userguardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuardianMemo> guardianMemos;

    public Userguardian() {}

    public Userguardian(int userId, String name, String phoneNumber, String ID, String password,
                        String careTargetName, String careTargetPhoneNumber, List<GuardianMemo> guardianMemos) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.ID = ID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
