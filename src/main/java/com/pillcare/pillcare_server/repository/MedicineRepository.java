package com.pillcare.pillcare_server.repository;

import com.pillcare.pillcare_server.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    Medicine findByUserIdAndPillCaseColor(int userId, String pillCaseColor);
    List<Medicine> findAllByUserId(int userId);
}
