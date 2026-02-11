package com.project.househealth.repositories;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.HealthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthLogRepository extends JpaRepository<HealthLog, Long> {
}
