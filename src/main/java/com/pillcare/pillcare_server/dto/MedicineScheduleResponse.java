package com.pillcare.pillcare_server.dto;

import java.util.List;
import com.pillcare.pillcare_server.dto.MedicineRequest.Schedule;

public class MedicineScheduleResponse {
    private String medicineName;
    private String pillCaseColor;
    private List<Schedule> schedules;

    public MedicineScheduleResponse(String medicineName, String pillCaseColor, List<Schedule> schedules) {
        this.medicineName = medicineName;
        this.pillCaseColor = pillCaseColor;
        this.schedules = schedules;
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
