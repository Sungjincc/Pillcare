package com.pillcare.pillcare_server.repository;

import com.pillcare.pillcare_server.entity.Userguardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserguardianRepository extends JpaRepository<Userguardian, Integer> {
    Userguardian findByUserId(int userId);
    boolean existsByID(String ID);
    Userguardian findByID(String ID);
}
