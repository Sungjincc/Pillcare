package com.pillcare.pillcare_server.repository;

import com.pillcare.pillcare_server.entity.IntakeLog;
import com.pillcare.pillcare_server.entity.IntakeLogKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IntakeLogRepository extends JpaRepository<IntakeLog, IntakeLogKey> {
    IntakeLog findByUserIdAndPillCaseColorAndScheduledDateAndAlarmTime(
            int userId,
            String pillCaseColor,
            LocalDate scheduledDate,
            String alarmTime
    );

    boolean existsByUserIdAndPillCaseColorAndScheduledDateAndAlarmTimeAndDayOfWeek(
            int userId,
            String pillCaseColor,
            LocalDate scheduledDate,
            String alarmTime,
            String dayOfWeek
    );

    long deleteByUserIdAndPillCaseColor(int userId, String pillCaseColor);
}
