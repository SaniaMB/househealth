package com.project.househealth.repositories;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderSettingsRepository  extends JpaRepository<ReminderSettings, Long> {
    ReminderSettings findByUser(User user);
}
