package com.project.househealth.service;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;

import java.util.List;

public interface HealthLogService {
    // Blood Pressure Logging
    HealthLog recordBloodPressure(Long actingUserId,
                                  Integer systolic,
                                  Integer diastolic);

    // Blood Sugar Logging
    HealthLog recordBloodSugar(Long actingUserId,
                               Integer sugarValue,
                               SugarType sugarType);

    // Retrieve single log
    HealthLog getHealthLogById(Long id);

    // Retrieve current user's logs
    List<HealthLog> getMyLogs(Long actingUserId);

    // Retrieve logs filtered by metric
    List<HealthLog> getLogsByMetric(Long actingUserId,
                                    MetricType metricType);

    // Family "For You" feed
    List<HealthLog> getFamilyFeed(Long familyId, Long actingUserId);
}
