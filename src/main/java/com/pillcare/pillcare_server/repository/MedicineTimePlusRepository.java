package com.pillcare.pillcare_server.repository;

import com.pillcare.pillcare_server.entity.MedicineTimePlus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineTimePlusRepository extends JpaRepository<MedicineTimePlus, Integer> {
}
