package com.pillcare.pillcare_server.dto;

import java.util.List;
import com.pillcare.pillcare_server.dto.MedicineRequest.Schedule;

public class MedicineScheduleUpdateRequest {
    private int userId;
    private String medicineName;
    private String pillCaseColor;
    private List<Schedule> schedules;

    public MedicineScheduleUpdateRequest(int userId, String medicineName, String pillCaseColor, List<Schedule> schedules) {
        this.userId = userId;
        this.medicineName = medicineName;
        this.pillCaseColor = pillCaseColor;
        this.schedules = schedules;
    }

    public int getUserId() {
        return userId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getPillCaseColor() {
        return pillCaseColor;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
