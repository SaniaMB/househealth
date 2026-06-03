package com.project.househealth.service;

import com.project.househealth.entity.ReminderSettings;
import com.project.househealth.entity.User;
import com.project.househealth.enums.FrequencyType;
import com.project.househealth.enums.MetricType;
import com.project.househealth.exception.InvalidReminderConfigurationException;
import com.project.househealth.exception.ReminderSettingsNotFoundException;
import com.project.househealth.repositories.ReminderSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReminderSettingsServiceImpl implements ReminderSettingsService{

    private final ReminderSettingsRepository reminderSettingsRepository;
    private final CurrentUserService currentUserService;

    public ReminderSettingsServiceImpl(ReminderSettingsRepository reminderSettingsRepository,CurrentUserService currentUserService){
        this.reminderSettingsRepository = reminderSettingsRepository;
        this.currentUserService = currentUserService;
    }

   private  void validateReminderConfiguration(FrequencyType frequency, Integer customIntervalDays
    ){
       if (frequency == null) {
           throw new InvalidReminderConfigurationException(
                   "Frequency type cannot be null"
           );
       }

       if (frequency == FrequencyType.CUSTOM) {

           if (customIntervalDays == null) {
               throw new InvalidReminderConfigurationException(
                       "Custom interval days is required for CUSTOM frequency"
               );
           }

           if (customIntervalDays <= 0) {
               throw new InvalidReminderConfigurationException(
                       "Custom interval days must be greater than 0"
               );
           }

       } else {

           if (customIntervalDays != null) {
               throw new InvalidReminderConfigurationException(
                       "Custom interval days must be null for non-custom frequencies"
               );
           }
       }
   }

    @Override
    public ReminderSettings configureReminder(MetricType metricType,
                                              FrequencyType frequency, Integer customIntervalDays,
                                              boolean enabled){

        User currentUser = currentUserService.getCurrentUser();

        if(metricType == null) {
            throw new InvalidReminderConfigurationException("Metric type cannot be null");
        }

        validateReminderConfiguration(frequency, customIntervalDays);

        Optional<ReminderSettings> existingSettings = reminderSettingsRepository
                                                      .findByUser_UserIdAndMetricType(currentUser.getUserId(), metricType);

        if (existingSettings.isPresent()) {

            ReminderSettings reminderSettings = existingSettings.get();

            reminderSettings.setFrequencyType(frequency);
            reminderSettings.setFrequencyInterval(customIntervalDays);
            reminderSettings.setNotificationsEnabled(enabled);

            return reminderSettingsRepository.save(reminderSettings);
        }

        ReminderSettings reminderSettings = new ReminderSettings(metricType, frequency, currentUser);

        reminderSettings.setFrequencyInterval(customIntervalDays);
        reminderSettings.setNotificationsEnabled(enabled);

        return reminderSettingsRepository.save(reminderSettings);
    }

    @Override
    public ReminderSettings getReminderSettings(MetricType metricType){
        if(metricType == null){
            throw new InvalidReminderConfigurationException("Metric type cannot be null");
        }

        User currentUser = currentUserService.getCurrentUser();

        return reminderSettingsRepository.findByUser_UserIdAndMetricType(currentUser.getUserId(), metricType)
                                         .orElseThrow(() -> new ReminderSettingsNotFoundException("Reminder setting not found"));
    }
}
