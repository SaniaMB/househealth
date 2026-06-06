package com.project.househealth.service;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;

import java.util.List;

public interface HealthLogService {
    // Blood Pressure Logging
    HealthLog recordBloodPressure(Integer systolic,
                                  Integer diastolic);

    // Blood Sugar Logging
    HealthLog recordBloodSugar(Integer sugarValue,
                               SugarType sugarType);

    // Retrieve single log
    HealthLog getHealthLogById(Long id);

    // Retrieve current user's logs
    List<HealthLog> getMyLogs();

    // Retrieve logs filtered by metric
    List<HealthLog> getLogsByMetric(MetricType metricType);

    // Family "For You" feed
    List<HealthLog> getFamilyFeed(Long familyId);

    HealthLog getLatestBloodPressure();

    HealthLog getLatestFastingSugar();

    HealthLog getLatestPostMealSugar();
}
