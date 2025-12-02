package com.pillcare.pillcare_server.repository;

import com.pillcare.pillcare_server.entity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {

    List<MedicineSchedule> findByUserId(int userId);

    List<MedicineSchedule> findAllByDayOfWeekAndAlarmTime(String dayOfWeek, String alarmTime);

    List<MedicineSchedule> findByDayOfWeek(String dayOfWeek);

    List<MedicineSchedule> findByUserIdAndPillCaseColor(int userId, String pillCaseColor);

    long deleteByUserIdAndPillCaseColor(int userId, String pillCaseColor);
}
