package com.project.househealth.dashboard;

import java.time.LocalDate;

public class TrendDataPointResponse {

    private LocalDate date;

    private Double value;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}