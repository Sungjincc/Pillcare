package com.pillcare.pillcare_server.dto;

public class LoginRequest {
    private String ID;
    private String password;

    public LoginRequest(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }
}
