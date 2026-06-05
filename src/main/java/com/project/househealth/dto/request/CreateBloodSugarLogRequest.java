package com.project.househealth.dto.request;

import com.project.househealth.enums.SugarType;
import jakarta.validation.constraints.NotNull;

public class CreateBloodSugarLogRequest {

    @NotNull(message = "Sugar type is required")
    private SugarType sugarType;

    @NotNull(message = "Sugar value is required")
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
