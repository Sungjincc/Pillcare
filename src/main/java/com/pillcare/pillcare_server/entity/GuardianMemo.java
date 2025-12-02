package com.pillcare.pillcare_server.entity;

import jakarta.persistence.*;

@Entity
public class GuardianMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memoId;

    private Integer memoIndex;  // 유저별 메모 번호

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Userguardian userguardian;

    public GuardianMemo() {}

    public GuardianMemo(int memoId, int memoIndex, String content, Userguardian userguardian) {
        this.memoId = memoId;
        this.memoIndex = memoIndex;
        this.content = content;
        this.userguardian = userguardian;
    }

    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public int getMemoIndex() {
        return memoIndex;
    }

    public void setMemoIndex(int memoIndex) {
        this.memoIndex = memoIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Userguardian getUserguardian() {
        return userguardian;
    }

    public void setUserguardian(Userguardian userguardian) {
        this.userguardian = userguardian;
    }
}
