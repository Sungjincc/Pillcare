package com.pillcare.pillcare_server.dto;

public class PasswordResponse {
    private String password;

    public PasswordResponse() {}

    public PasswordResponse(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
