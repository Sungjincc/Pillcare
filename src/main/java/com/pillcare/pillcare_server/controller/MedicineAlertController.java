package com.pillcare.pillcare_server.controller;

import com.pillcare.pillcare_server.dto.MedicineTakenRequestDTO;
import com.pillcare.pillcare_server.service.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MedicineAlertController {

    private final MedicationService medicationService;

    public MedicineAlertController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // 복약 알림 조회 - 특정 사용자 ID를 받아 알림 대상 약 통 색상을 확인
    @GetMapping("/medicationAlert/{userId}")
    public ResponseEntity<String> getMedicationAlert(@PathVariable int userId) {
        String result = String.join(",", medicationService.getMedicationsToAlert(userId));
        if (!result.isEmpty()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // 복약 기록 저장 - 사용자가 약을 복용했다고 기록
    @PostMapping("/medicationAlert")
    public ResponseEntity<String> postMedicationTaken(@RequestBody MedicineTakenRequestDTO request) {
        boolean updated = medicationService.logMedicationTaken(
            request.getUserId(),
            request.getPillCaseColor()
        );

        if (updated) {
            return ResponseEntity.ok("복약 기록이 저장되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("알림 로그가 없습니다.");
        }
    }
}
