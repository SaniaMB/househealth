package com.project.househealth.repositories;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface HealthLogRepository extends JpaRepository<HealthLog, Long> {
    List<HealthLog> findByUser_UserIdOrderByLoggedAtDesc(Long userId);

    List<HealthLog> findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(Long userId, MetricType metricType);

    List<HealthLog> findByUserInOrderByLoggedAtDesc(List<User> users);

    List<HealthLog> findByUser_UserIdAndMetricTypeAndLoggedAtBetween(
            Long userId,
            MetricType metricType,
            Instant startDate,
            Instant endDate
    );

    List<HealthLog> findByUser_UserIdAndMetricTypeAndSugarTypeAndLoggedAtBetween(
            Long userId,
            MetricType metricType,
            SugarType sugarType,
            Instant startDate,
            Instant endDate
    );

    Optional<HealthLog> findFirstByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(
            Long userId,
            MetricType metricType
    );

    Optional<HealthLog> findFirstByUser_UserIdAndMetricTypeAndSugarTypeOrderByLoggedAtDesc(
            Long userId,
            MetricType metricType,
            SugarType sugarType
    );

    List<HealthLog> findByUser_UserIdInOrderByLoggedAtDesc(
            List<Long> userIds
    );
}
