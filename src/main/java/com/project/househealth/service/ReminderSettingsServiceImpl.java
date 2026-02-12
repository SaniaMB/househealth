package com.project.househealth.service;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.repositories.ReminderSettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class ReminderSettingsServiceImpl implements ReminderSettingsService{

    private final ReminderSettingsRepository reminderSettingsRepository;

    public ReminderSettingsServiceImpl(ReminderSettingsRepository reminderSettingsRepository){
        this.reminderSettingsRepository = reminderSettingsRepository;
    }

    public ReminderSettings createReminderSettings(ReminderSettings reminderSettings) {
        return reminderSettingsRepository.save(reminderSettings);
    }

    @Override
    public ReminderSettings getReminderSettingById(Long id) {
        return reminderSettingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder setting not found"));
    }
}
