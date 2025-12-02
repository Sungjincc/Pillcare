package com.pillcare.pillcare_server.dto;

public class LoginResponse {
    private int userId;
    private String token;
    private String name;

    public LoginResponse(int userId, String token, String name) {
        this.userId = userId;
        this.token = token;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
