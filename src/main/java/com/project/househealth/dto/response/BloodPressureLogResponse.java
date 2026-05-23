package com.project.househealth.dto.response;

import java.time.Instant;

public class BloodPressureLogResponse {

    private Long logId;
    private Long userId;
    private Integer systolic;
    private Integer diastolic;
    private Instant loggedAt;

    public BloodPressureLogResponse(){}


    public Long getLogId() {
        return logId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public Instant getLoggedAt() {
        return loggedAt;
    }

    public void setLogId(Long logId){
        this.logId = logId;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }
    public void setSystolic(Integer systolic){
        this.systolic = systolic;
    }
    public void setDiastolic(Integer diastolic){
        this.diastolic = diastolic;
    }
    public void setLoggedAt(Instant loggedAt){
        this.loggedAt = loggedAt;
    }
}
