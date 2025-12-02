package com.pillcare.pillcare_server.dto;

public class UpdateGuardianMemoRequest {
    private String content;

    public UpdateGuardianMemoRequest() {}

    public UpdateGuardianMemoRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}