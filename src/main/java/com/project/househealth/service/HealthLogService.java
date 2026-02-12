package com.project.househealth.service;

import com.project.househealth.entity.HealthLog;

public interface HealthLogService {
    HealthLog createHealthLog(HealthLog healthLog);
    HealthLog getHealthLogById(Long id);
}
