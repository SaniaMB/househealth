package com.project.househealth.service;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.repositories.HealthLogRepository;
import org.springframework.stereotype.Service;

@Service
public class HealthLogServiceImpl implements HealthLogService{

    private final HealthLogRepository healthLogRepository;

    public HealthLogServiceImpl(HealthLogRepository healthLogRepository){
        this.healthLogRepository = healthLogRepository;
    }

    @Override
    public HealthLog createHealthLog(HealthLog healthLog) {
        return healthLogRepository.save(healthLog);
    }

    @Override
    public HealthLog getHealthLogById(Long id) {
        return healthLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Health log not found"));
    }
}
