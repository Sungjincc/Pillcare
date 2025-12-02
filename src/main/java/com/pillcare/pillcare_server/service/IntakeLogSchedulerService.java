package com.pillcare.pillcare_server.service;

import com.pillcare.pillcare_server.entity.IntakeLog;
import com.pillcare.pillcare_server.entity.PillboxStatus;
import com.pillcare.pillcare_server.repository.IntakeLogRepository;
import com.pillcare.pillcare_server.repository.MedicineScheduleRepository;
import com.pillcare.pillcare_server.entity.MedicineSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class IntakeLogSchedulerService {

    private final MedicineScheduleRepository medicineScheduleRepository;
    private final IntakeLogRepository intakeLogRepository;
    private static final Logger logger = LoggerFactory.getLogger(IntakeLogSchedulerService.class);

    public IntakeLogSchedulerService(MedicineScheduleRepository medicineScheduleRepository,
                                     IntakeLogRepository intakeLogRepository) {
        this.medicineScheduleRepository = medicineScheduleRepository;
        this.intakeLogRepository = intakeLogRepository;
    }

    // 1분마다 실행
    @Scheduled(cron = "0 * * * * *")
    public void createTodayIntakeLogs() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        String alarmTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String dayOfWeek = now.getDayOfWeek().toString().substring(0, 3).toLowerCase();
        dayOfWeek = Character.toUpperCase(dayOfWeek.charAt(0)) + dayOfWeek.substring(1);  // 첫 글자만 대문자

        List<MedicineSchedule> schedules = medicineScheduleRepository
                .findAllByDayOfWeekAndAlarmTime(dayOfWeek, alarmTime);

        for (MedicineSchedule schedule : schedules) {
            boolean exists = intakeLogRepository
                    .existsByUserIdAndPillCaseColorAndScheduledDateAndAlarmTimeAndDayOfWeek(
                            schedule.getUserId(),
                            schedule.getPillCaseColor(),
                            today,
                            alarmTime,
                            dayOfWeek
                    );

            if (!exists) {
            	IntakeLog log = new IntakeLog(
        		    schedule.getUserId(),
        		    schedule.getPillCaseColor(),
        		    alarmTime,
        		    dayOfWeek,
        		    today,
        		    false,
        		    null,
        		    PillboxStatus.OPEN
        		);

                intakeLogRepository.save(log);

                logger.info("intake_log 자동생성: userId={}, color={}, time={}, day={}",
                        schedule.getUserId(), schedule.getPillCaseColor(), alarmTime, dayOfWeek);
            }
        }
    }
}
