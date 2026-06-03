package com.project.househealth.controllers;

import com.project.househealth.dto.request.ReminderSettingsRequest;
import com.project.househealth.dto.response.ReminderSettingsResponse;
import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.enums.MetricType;
import com.project.househealth.service.ReminderSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
public class ReminderSettingsController {

    private final ReminderSettingsService reminderSettingsService;


    public ReminderSettingsController(ReminderSettingsService reminderSettingsService) {
        this.reminderSettingsService = reminderSettingsService;
    }


    @PostMapping("/configure-reminder")
    public ResponseEntity<ReminderSettingsResponse> configureReminder(
            @RequestBody ReminderSettingsRequest request
    ){

        ReminderSettings reminderSettings = reminderSettingsService.configureReminder(
                                                request.getMetricType(),
                                                request.getFrequencyType(),
                                                request.getFrequencyInterval(),
                                                request.isNotificationsEnabled());


        ReminderSettingsResponse response = new ReminderSettingsResponse();

        response.setReminderId(reminderSettings.getReminderId());
        response.setFrequencyType(reminderSettings.getFrequencyType());
        response.setFrequencyInterval(reminderSettings.getFrequencyInterval());
        response.setNotificationsEnabled(reminderSettings.getNotificationsEnabled());
        response.setMetricType(reminderSettings.getMetricType());
        response.setLastTriggeredAt(reminderSettings.getLastTriggeredAt());


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/settings")
    public ResponseEntity<ReminderSettingsResponse> getReminderSettings(
                @RequestParam MetricType metricType
    ){

        ReminderSettings reminderSettings =
                        reminderSettingsService.getReminderSettings(metricType);

        ReminderSettingsResponse response = new ReminderSettingsResponse();

        response.setReminderId(reminderSettings.getReminderId());
        response.setFrequencyType(reminderSettings.getFrequencyType());
        response.setFrequencyInterval(reminderSettings.getFrequencyInterval());
        response.setNotificationsEnabled(reminderSettings.getNotificationsEnabled());
        response.setMetricType(reminderSettings.getMetricType());
        response.setLastTriggeredAt(reminderSettings.getLastTriggeredAt());


        return ResponseEntity.ok(response);
    }
}
