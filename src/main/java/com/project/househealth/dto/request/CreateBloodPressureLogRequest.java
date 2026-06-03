package com.project.househealth.dto.request;

public class CreateBloodPressureLogRequest {

    private Integer systolic;
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
