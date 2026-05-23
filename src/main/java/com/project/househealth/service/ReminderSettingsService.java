package com.project.househealth.service;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.enums.FrequencyType;
import com.project.househealth.enums.MetricType;

public interface ReminderSettingsService {
    ReminderSettings configureReminder(
            Long actingUserId,
            MetricType metricType,
            FrequencyType frequency,
            Integer customIntervalDays,
            boolean enabled
    );

    ReminderSettings getReminderSettings(
            Long actingUserId,
            MetricType metricType
    );
}
