package com.project.househealth.dto.request;

import jakarta.validation.constraints.NotNull;

public class CreateBloodPressureLogRequest {

    @NotNull(message = "Systolic value is required")
    private Integer systolic;

    @NotNull(message = "Diastolic value is required")
    private Integer diastolic;

    public CreateBloodPressureLogRequest(){}

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }


    public Integer getSystolic() {
        return systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }
}
