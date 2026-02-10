package com.project.househealth.entity;

import com.project.househealth.enums.SugarType;
import jakarta.persistence.*;
import com.project.househealth.enums.MetricType;

import java.time.Instant;

@Entity
@Table(name = "health_logs")
public class HealthLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType;

    @Enumerated(EnumType.STRING)
    private SugarType sugarType;


    private Integer systolic;
    private Integer diastolic;
    private Integer sugarValue;

    @Column(nullable = false)
    private Instant loggedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected HealthLog() {}

    // Proper constructor (only mandatory fields)
    public HealthLog(MetricType metricType, Instant loggedAt, User user) {
        this.metricType = metricType;
        this.loggedAt = loggedAt;
        this.user = user;
    }

    public Long getLogId() {
        return logId;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public SugarType getSugarType() {
        return sugarType;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public Integer getSugarValue() {
        return sugarValue;
    }

    public Instant getLoggedAt() {
        return loggedAt;
    }

    /*
     * ⚠️ These setters are acceptable FOR NOW.
     * Later, these should become domain-specific methods:
     * - recordBloodPressure(...)
     * - recordSugar(...)
     */
    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public void setSugarType(SugarType sugarType) {
        this.sugarType = sugarType;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public void setSugarValue(Integer sugarValue) {
        this.sugarValue = sugarValue;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
