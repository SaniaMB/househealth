package com.project.househealth.service;

import com.project.househealth.entity.ReminderSettings;

public interface ReminderSettingsService {
    ReminderSettings createReminderSettings(ReminderSettings reminderSettings);
    ReminderSettings getReminderSettingById(Long id);
}
