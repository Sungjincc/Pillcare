package com.pillcare.pillcare_server.dto;

public class CheckIdResponse {
    private boolean exists;

    public CheckIdResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }
}
