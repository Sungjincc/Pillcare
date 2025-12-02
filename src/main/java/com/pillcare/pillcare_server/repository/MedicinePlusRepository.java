package com.capstone.pillcare.repository;

import com.pillcare.pillcare_server.entity.MedicinePlus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicinePlusRepository extends JpaRepository<MedicinePlus, Integer> {
}
