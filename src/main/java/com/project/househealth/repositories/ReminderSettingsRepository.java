package com.project.househealth.repositories;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.enums.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReminderSettingsRepository  extends JpaRepository<ReminderSettings, Long> {
//    ReminderSettings findByUser(User user);

    List<ReminderSettings> findByNotificationsEnabledTrue();

    Optional<ReminderSettings>
    findByUser_UserIdAndMetricType(
            Long userId,
            MetricType metricType
    );
}
