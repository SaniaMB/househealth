package com.project.househealth.scheduler;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.repositories.ReminderSettingsRepository;
import com.project.househealth.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderSettingsRepository reminderSettingsRepository;
    private final NotificationService notificationService;

    public ReminderScheduler(
            ReminderSettingsRepository reminderSettingsRepository,
            NotificationService notificationService
    ) {
        this.reminderSettingsRepository = reminderSettingsRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 60000)
    public void processReminders() {

        List<ReminderSettings> reminders =
                reminderSettingsRepository
                        .findByNotificationsEnabledTrue();

        for (ReminderSettings reminder : reminders) {

            if (isReminderDue(reminder)) {

                String title = reminder.getMetricType() + " Reminder";

                String message;

                switch (reminder.getMetricType()) {

                    case BP:
                        message = "Time to record your blood pressure reading.";
                        break;

                    case SUGAR:
                        message = "Time to record your blood sugar reading.";
                        break;

                    default:
                        message = "Health log reminder.";
                }

                notificationService.createNotification(reminder.getUser(), title, message);
                reminder.markTriggered();
                reminderSettingsRepository.save(reminder);
            }
        }
    }

    private boolean isReminderDue(
            ReminderSettings reminder
    ) {

        Instant lastTriggeredAt = reminder.getLastTriggeredAt();

        if (lastTriggeredAt == null) {
            return true;
        }

        Instant now = Instant.now();

        long daysElapsed = Duration
                        .between(lastTriggeredAt, now)
                        .toDays();

        switch (reminder.getFrequencyType()) {

            case DAILY:
                return daysElapsed >= 1;

            case WEEKLY:
                return daysElapsed >= 7;

            case MONTHLY:
                return daysElapsed >= 30;

            case CUSTOM:
                return daysElapsed >=
                        reminder.getFrequencyInterval();

            default:
                return false;
        }
    }

}
