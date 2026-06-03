package com.project.househealth.dto.request;

import com.project.househealth.enums.SugarType;

public class CreateBloodSugarLogRequest {

    private SugarType sugarType;
    private Integer sugarValue;

    public CreateBloodSugarLogRequest(){}


    public void setSugarType(SugarType sugarType) {
        this.sugarType = sugarType;
    }

    public void setSugarValue(Integer sugarValue) {
        this.sugarValue = sugarValue;
    }


    public SugarType getSugarType() {
        return sugarType;
    }

    public Integer getSugarValue() {
        return sugarValue;
    }
}
