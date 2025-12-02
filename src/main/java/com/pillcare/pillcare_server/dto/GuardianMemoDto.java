package com.pillcare.pillcare_server.dto;

public class GuardianMemoDto {
    private int memoId;
    private int userId;
    private String content;

    public GuardianMemoDto(int memoId, int userId, String content) {
        this.memoId = memoId;
        this.userId = userId;
        this.content = content;
    }

    public int getMemoId() { return memoId; }
    public int getUserId() { return userId; }
    public String getContent() { return content; }
}
