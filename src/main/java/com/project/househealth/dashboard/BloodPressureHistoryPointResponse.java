package com.project.househealth.dashboard;

import java.time.LocalDate;

public class BloodPressureHistoryPointResponse {

    private LocalDate date;

    private Double systolic;

    private Double diastolic;;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getSystolic() {
        return systolic;
    }

    public void setSystolic(Double systolic) {
        this.systolic = systolic;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Double diastolic) {
        this.diastolic = diastolic;
    }
}