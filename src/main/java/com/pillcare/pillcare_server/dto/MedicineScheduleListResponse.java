package com.pillcare.pillcare_server.dto;

import java.util.List;

public class MedicineScheduleListResponse {
    private List<MedicineScheduleResponse> medicineList;

    public MedicineScheduleListResponse(List<MedicineScheduleResponse> medicineList) {
        this.medicineList = medicineList;
    }

    public List<MedicineScheduleResponse> getMedicineList() { return medicineList; }
}