package com.pillcare.pillcare_server.controller;

import com.pillcare.pillcare_server.dto.MedicineRequest;
import com.pillcare.pillcare_server.entity.*;
import com.pillcare.pillcare_server.repository.IntakeLogRepository;
import com.pillcare.pillcare_server.repository.MedicineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {

    private final MedicineRepository medicineRepository;
    private final IntakeLogRepository intakeLogRepository;

    public MedicineController(MedicineRepository medicineRepository, IntakeLogRepository intakeLogRepository) {
        this.medicineRepository = medicineRepository;
        this.intakeLogRepository = intakeLogRepository;
    }

    @PostMapping
    public ResponseEntity<Medicine> registerMedicine(@RequestBody MedicineRequest request) {
        Medicine medicine = new Medicine(
            request.getUserId(),
            request.getMedicineName(),
            request.getPillCaseColor()
        );

        Medicine savedMedicine = medicineRepository.save(medicine);

        List<MedicineSchedule> scheduleList = new ArrayList<>();
        for (MedicineRequest.Schedule schedule : request.getSchedules()) {
            for (String day : schedule.getDaysOfWeek()) {
                MedicineSchedule item = new MedicineSchedule(
                    request.getUserId(),
                    request.getPillCaseColor(),
                    day,
                    schedule.getTime(),
                    savedMedicine
                );
                scheduleList.add(item);
            }
        }

        savedMedicine.getScheduleList().addAll(scheduleList);
        medicineRepository.save(savedMedicine);

        LocalDate today = LocalDate.now();
        List<IntakeLog> logs = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            LocalDate date = today.plusDays(i);
            String dayStr = date.getDayOfWeek().name().substring(0, 3);
            dayStr = dayStr.substring(0, 1).toUpperCase() + dayStr.substring(1).toLowerCase();

            for (MedicineRequest.Schedule schedule : request.getSchedules()) {
                if (schedule.getDaysOfWeek().contains(dayStr)) {
                    logs.add(new IntakeLog(
                        request.getUserId(),
                        request.getPillCaseColor(),
                        schedule.getTime(),
                        dayStr,
                        date,
                        false,
                        null,
                        PillboxStatus.CLOSED
                    ));
                }
            }
        }

        intakeLogRepository.saveAll(logs);
        return ResponseEntity.ok(savedMedicine);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Medicine>> getMedicines(@PathVariable int userId) {
        return ResponseEntity.ok(medicineRepository.findAllByUserId(userId));
    }
}
