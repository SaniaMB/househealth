package com.project.househealth.repositories;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReminderSettingsRepository  extends JpaRepository<ReminderSettings, Long> {
    ReminderSettings findByUser(User user);
    Optional<ReminderSettings>
    findByUser_UserIdAndMetricType(
            Long userId,
            MetricType metricType
    );
}
