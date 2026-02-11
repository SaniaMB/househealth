package com.project.househealth.entity;

import com.project.househealth.enums.FrequencyType;
import com.project.househealth.enums.MetricType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reminder_settings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "metric_type"})
        })
public class ReminderSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyType frequencyType;

    @Column(name = "frequency_value")
    private Integer frequencyInterval;
    private Instant lastTriggeredAt;

    @Column(name = "active", nullable = false)
    private boolean notificationsEnabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected ReminderSettings(){}

    public ReminderSettings(MetricType metricType, FrequencyType frequencyType, User user){
        this.metricType = metricType;
        this.frequencyType = frequencyType;
        this.user = user;
    }

    public void markTriggered() {
        this.lastTriggeredAt = Instant.now();
    }

    public Long getReminderId() {
        return reminderId;
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

    public Instant getLastTriggeredAt() {
        return lastTriggeredAt;
    }

    public boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public User getUser() {
        return user;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public void setFrequencyInterval(Integer frequencyValue) {
        this.frequencyInterval = frequencyValue;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

}
