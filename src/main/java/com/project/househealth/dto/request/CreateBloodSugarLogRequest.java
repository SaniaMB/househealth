package com.project.househealth.dto.request;

import com.project.househealth.enums.SugarType;

public class CreateBloodSugarLogRequest {
    private Long actingUserId;
    private SugarType sugarType;
    private Integer sugarValue;

    public CreateBloodSugarLogRequest(){}

    public void setActingUserId(Long actingUserId) {
        this.actingUserId = actingUserId;
    }

    public void setSugarType(SugarType sugarType) {
        this.sugarType = sugarType;
    }

    public void setSugarValue(Integer sugarValue) {
        this.sugarValue = sugarValue;
    }

    public Long getActingUserId() {
        return actingUserId;
    }

    public SugarType getSugarType() {
        return sugarType;
    }

    public Integer getSugarValue() {
        return sugarValue;
    }
}
