package com.pillcare.pillcare_server.dto;

import java.util.List;

public class MedicineRequest {
    private int userId;
    private String medicineName;
    private String pillCaseColor;
    private List<Schedule> schedules;

    public MedicineRequest(int userId, String medicineName, String pillCaseColor, List<Schedule> schedules) {
        this.userId = userId;
        this.medicineName = medicineName;
        this.pillCaseColor = pillCaseColor;
        this.schedules = schedules;
    }

    public int getUserId() { return userId; }
    public String getMedicineName() { return medicineName; }
    public String getPillCaseColor() { return pillCaseColor; }
    public List<Schedule> getSchedules() { return schedules; }

    // ✅ 내부 static 클래스로 수정
    public static class Schedule {
        private String time;
        private List<String> daysOfWeek;

        public Schedule(String time, List<String> daysOfWeek) {
            this.time = time;
            this.daysOfWeek = daysOfWeek;
        }

        public String getTime() { return time; }
        public List<String> getDaysOfWeek() { return daysOfWeek; }
    }
}