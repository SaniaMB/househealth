package com.project.househealth.repositories;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthLogRepository extends JpaRepository<HealthLog, Long> {
    List<HealthLog> findByUser_UserIdOrderByLoggedAtDesc(Long userId);
    List<HealthLog> findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(Long userId, MetricType metricType);
    List<HealthLog> findByUserInOrderByLoggedAtDesc(List<User> users);
}
