package com.pillcare.pillcare_server.service;

import com.pillcare.pillcare_server.entity.IntakeLog;
import com.pillcare.pillcare_server.entity.Medicine;
import com.pillcare.pillcare_server.entity.MedicineSchedule;
import com.pillcare.pillcare_server.entity.PillboxStatus;
import com.pillcare.pillcare_server.repository.IntakeLogRepository;
import com.pillcare.pillcare_server.repository.MedicineRepository;
import com.pillcare.pillcare_server.repository.MedicineScheduleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MedicationService {

    private final MedicineRepository medicineRepository;
    private final MedicineScheduleRepository scheduleRepository;
    private final IntakeLogRepository intakeLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(MedicationService.class);

    public MedicationService(MedicineRepository medicineRepository,
                             MedicineScheduleRepository scheduleRepository,
                             IntakeLogRepository intakeLogRepository) {
        this.medicineRepository = medicineRepository;
        this.scheduleRepository = scheduleRepository;
        this.intakeLogRepository = intakeLogRepository;
    }

    public List<String> getMedicationsToAlert(int userId) {
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String currentDay = capitalizeFirst(now.getDayOfWeek().toString().substring(0, 3).toLowerCase(Locale.ROOT));

        logger.info("현재 시간: {}", currentTime);
        logger.info("현재 요일: {}", currentDay);

        List<Medicine> medicines = medicineRepository.findAllByUserId(userId);

        List<Medicine> matched = medicines.stream()
                .filter(medicine ->
                        medicine.getScheduleList().stream()
                                .anyMatch(schedule ->
                                        Objects.equals(schedule.getAlarmTime(), currentTime) &&
                                                Objects.equals(schedule.getDayOfWeek(), currentDay)))
                .collect(Collectors.toList());

        logger.info("알림 대상 약 개수: {}", matched.size());

        return matched.stream()
                .map(Medicine::getPillCaseColor)
                .collect(Collectors.toList());
    }

    public boolean logMedicationTaken(int userId, String pillColor) {
        LocalDateTime now = LocalDateTime.now();
        String alarmTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String dayOfWeek = capitalizeFirst(now.getDayOfWeek().toString().substring(0, 3).toLowerCase(Locale.ROOT));
        LocalDate today = now.toLocalDate();

        Medicine medicine = medicineRepository.findAllByUserId(userId).stream()
                .filter(m -> Objects.equals(m.getPillCaseColor(), pillColor))
                .findFirst()
                .orElse(null);

        if (medicine == null) {
            logger.warn("해당 색상의 약을 찾을 수 없음: {}", pillColor);
            return false;
        }

        IntakeLog log = intakeLogRepository.findByUserIdAndPillCaseColorAndScheduledDateAndAlarmTime(
                userId, pillColor, today, alarmTime
        );

        if (log != null) {
            log.setTaken(true);
            log.setTakenTime(now);
            log.setStatus(PillboxStatus.CLOSED);
            intakeLogRepository.save(log);
            logger.info("복약 완료 처리(업데이트): userId={}, color={}", userId, pillColor);
            return true;
        } else {
        	IntakeLog newLog = new IntakeLog(
    		    userId,
    		    pillColor,
    		    alarmTime,
    		    dayOfWeek,
    		    today,
    		    true,
    		    now,
    		    PillboxStatus.CLOSED
    		);
            intakeLogRepository.save(newLog);
            logger.info("복약 기록 신규 생성: userId={}, color={}", userId, pillColor);
            return true;
        }
    }

    private String capitalizeFirst(String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }
}
