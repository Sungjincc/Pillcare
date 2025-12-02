package com.pillcare.pillcare_server.controller;

import com.pillcare.pillcare_server.dto.*;
import com.pillcare.pillcare_server.dto.MedicineRequest.Schedule;
import com.pillcare.pillcare_server.entity.*;
import com.pillcare.pillcare_server.repository.IntakeLogRepository;
import com.pillcare.pillcare_server.repository.MedicineRepository;
import com.pillcare.pillcare_server.repository.MedicineScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/medicine-schedule")
public class MedicineScheduleController {

    private final MedicineRepository medicineRepository;
    private final IntakeLogRepository intakeLogRepository;
    private final MedicineScheduleRepository medicineScheduleRepository;

    public MedicineScheduleController(MedicineRepository medicineRepository, IntakeLogRepository intakeLogRepository, MedicineScheduleRepository medicineScheduleRepository) {
        this.medicineRepository = medicineRepository;
        this.intakeLogRepository = intakeLogRepository;
        this.medicineScheduleRepository = medicineScheduleRepository;
    }

    @PostMapping
    public ResponseEntity<Void> sendSchedule(@RequestBody MedicineRequest request) {
        saveMedicineAndLogs(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateSchedule(@RequestBody MedicineScheduleUpdateRequest request) {
        Medicine target = medicineRepository.findByUserIdAndPillCaseColor(request.getUserId(), request.getPillCaseColor());
        if (target == null) return ResponseEntity.notFound().build();

        medicineRepository.delete(target);
        medicineScheduleRepository.deleteByUserIdAndPillCaseColor(request.getUserId(), request.getPillCaseColor());
        intakeLogRepository.deleteByUserIdAndPillCaseColor(request.getUserId(), request.getPillCaseColor());

        MedicineRequest newRequest = new MedicineRequest(
            request.getUserId(),
            request.getMedicineName(),
            request.getPillCaseColor(),
            request.getSchedules()
        );
        saveMedicineAndLogs(newRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{pillCaseColor}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable int userId,
                                               @PathVariable String pillCaseColor) {
        Medicine medicine = medicineRepository.findByUserIdAndPillCaseColor(userId, pillCaseColor);
        if (medicine == null) return ResponseEntity.notFound().build();

        medicineRepository.delete(medicine);
        medicineScheduleRepository.deleteByUserIdAndPillCaseColor(userId, pillCaseColor);
        intakeLogRepository.deleteByUserIdAndPillCaseColor(userId, pillCaseColor);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MedicineScheduleListResponse> getSchedules(@PathVariable int userId) {
        List<Medicine> medicines = medicineRepository.findAllByUserId(userId);
        List<MedicineScheduleResponse> results = new ArrayList<>();

        for (Medicine medicine : medicines) {
            Map<String, List<MedicineSchedule>> grouped = new HashMap<>();
            for (MedicineSchedule schedule : medicine.getScheduleList()) {
                grouped.computeIfAbsent(schedule.getAlarmTime(), k -> new ArrayList<>()).add(schedule);
            }

            List<Schedule> schedules = new ArrayList<>();
            for (Map.Entry<String, List<MedicineSchedule>> entry : grouped.entrySet()) {
                List<String> days = new ArrayList<>();
                for (MedicineSchedule item : entry.getValue()) {
                    if (!days.contains(item.getDayOfWeek())) {
                        days.add(item.getDayOfWeek());
                    }
                }
                schedules.add(new Schedule(entry.getKey(), days));
            }

            results.add(new MedicineScheduleResponse(
                medicine.getMedicineName(),
                medicine.getPillCaseColor(),
                schedules
            ));
        }

        return ResponseEntity.ok(new MedicineScheduleListResponse(results));
    }

    @Transactional
    private Medicine saveMedicineAndLogs(MedicineRequest request) {
        medicineScheduleRepository.deleteByUserIdAndPillCaseColor(request.getUserId(), request.getPillCaseColor());
        intakeLogRepository.deleteByUserIdAndPillCaseColor(request.getUserId(), request.getPillCaseColor());

        Medicine medicine = new Medicine(
            request.getUserId(),
            request.getMedicineName(),
            request.getPillCaseColor()
        );
        Medicine saved = medicineRepository.save(medicine);

        List<MedicineSchedule> scheduleList = new ArrayList<>();
        for (Schedule schedule : request.getSchedules()) {
            for (String day : schedule.getDaysOfWeek()) {
                scheduleList.add(new MedicineSchedule(
                    request.getUserId(),
                    request.getPillCaseColor(),
                    day,
                    schedule.getTime(),
                    saved
                ));
            }
        }
        medicineScheduleRepository.saveAll(scheduleList);

        saved.getScheduleList().addAll(scheduleList);
        medicineRepository.save(saved);

        LocalDate today = LocalDate.now();
        List<IntakeLog> logs = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            LocalDate date = today.plusDays(i);
            String dayStr = date.getDayOfWeek().name().substring(0, 3);
            dayStr = dayStr.substring(0, 1).toUpperCase() + dayStr.substring(1).toLowerCase();

            for (Schedule schedule : request.getSchedules()) {
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
        return saved;
    }
}
