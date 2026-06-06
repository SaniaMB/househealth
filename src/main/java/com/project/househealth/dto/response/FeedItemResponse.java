package com.project.househealth.dto.response;

import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;

import java.time.Instant;

public class FeedItemResponse {

    private Long userId;
    private String userName;

    private MetricType metricType;

    private Integer systolic;
    private Integer diastolic;

    private Integer sugarValue;
    private SugarType sugarType;

    private Instant loggedAt;

    public FeedItemResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public Integer getSugarValue() {
        return sugarValue;
    }

    public void setSugarValue(Integer sugarValue) {
        this.sugarValue = sugarValue;
    }

    public SugarType getSugarType() {
        return sugarType;
    }

    public void setSugarType(SugarType sugarType) {
        this.sugarType = sugarType;
    }

    public Instant getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(Instant loggedAt) {
        this.loggedAt = loggedAt;
    }
}