package com.project.househealth.dto.request;

import com.project.househealth.enums.FrequencyType;
import com.project.househealth.enums.MetricType;

public class ReminderSettingsRequest {

    private MetricType metricType;
    private FrequencyType frequencyType;
    private Integer frequencyInterval;
    private boolean notificationsEnabled = true;

    public ReminderSettingsRequest( ) {}

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public void setFrequencyInterval(Integer frequencyInterval) {
        this.frequencyInterval = frequencyInterval;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public FrequencyType getFrequencyType() {
        return frequencyType;
    }

    public Integer getFrequencyInterval() {
        return frequencyInterval;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

}
