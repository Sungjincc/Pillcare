package com.pillcare.pillcare_server.repository;

import com.pillcare.pillcare_server.entity.GuardianMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuardianMemoRepository extends JpaRepository<GuardianMemo, Integer> {
    List<GuardianMemo> findAllByUserguardianUserId(int userId);
    void deleteByUserguardianUserId(int userId);

    List<GuardianMemo> findByUserguardianUserIdOrderByMemoIndexAsc(int userId);
    int countByUserguardianUserId(int userId);

}
