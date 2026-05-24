package com.project.househealth.dto.request;

public class CreateBloodPressureLogRequest {
    private Long actingUserId;
    private Integer systolic;
    private Integer diastolic;

    public CreateBloodPressureLogRequest(){}

    public void setActingUserId(Long actingUserId) {
        this.actingUserId = actingUserId;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

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
