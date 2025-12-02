package com.pillcare.pillcare_server.model;

public class GuardianMemo {
    private String content = "";

    public GuardianMemo() {
    }

    public GuardianMemo(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
