package com.project.househealth.dto.response;

import com.project.househealth.enums.SugarType;

import java.time.Instant;

public class BloodSugarLogResponse {
    private Long logId;
    private Long userId;
    private Integer sugarValue;
    private SugarType sugarType;
    private Instant loggedAt;

    public BloodSugarLogResponse(){}

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSugarValue(Integer sugarValue) {
        this.sugarValue = sugarValue;
    }

    public void setSugarType(SugarType sugarType) {
        this.sugarType = sugarType;
    }

    public void setLoggedAt(Instant loggedAt) {
        this.loggedAt = loggedAt;
    }

    public Long getLogId() {
        return logId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getSugarValue() {
        return sugarValue;
    }

    public SugarType getSugarType() {
        return sugarType;
    }

    public Instant getLoggedAt() {
        return loggedAt;
    }
}
