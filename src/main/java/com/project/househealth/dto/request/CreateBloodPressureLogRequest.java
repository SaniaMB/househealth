package com.project.househealth.dto.request;

public class CreateBloodPressureLogRequest {
    private Long actingUserId;
    private Integer systolic;
    private Integer diastolic;

    public CreateBloodPressureLogRequest(){}

    public Long getActingUserId() {
        return actingUserId;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }
}
