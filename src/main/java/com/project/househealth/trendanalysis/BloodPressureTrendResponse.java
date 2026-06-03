package com.project.househealth.trendanalysis;

import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.enums.TrendStatus;

public class BloodPressureTrendResponse {

    TrendStatus trendStatus;
    private TrendPeriod trendPeriod;

    private Double currentAverageSystolic;
    private Double currentAverageDiastolic;

    private Double previousAverageSystolic;
    private Double previousAverageDiastolic;

    private Double systolicPercentageChange;
    private Double diastolicPercentageChange;

    public BloodPressureTrendResponse() {}

    public TrendStatus getTrendStatus() {
        return trendStatus;
    }

    public void setTrendStatus(TrendStatus trendStatus) {
        this.trendStatus = trendStatus;
    }

    public TrendPeriod getTrendPeriod() {
        return trendPeriod;
    }

    public void setTrendPeriod(TrendPeriod trendPeriod) {
        this.trendPeriod = trendPeriod;
    }

    public Double getCurrentAverageSystolic() {
        return currentAverageSystolic;
    }

    public void setCurrentAverageSystolic(Double currentAverageSystolic) {
        this.currentAverageSystolic = currentAverageSystolic;
    }

    public Double getCurrentAverageDiastolic() {
        return currentAverageDiastolic;
    }

    public void setCurrentAverageDiastolic(Double currentAverageDiastolic) {
        this.currentAverageDiastolic = currentAverageDiastolic;
    }

    public Double getPreviousAverageSystolic() {
        return previousAverageSystolic;
    }

    public void setPreviousAverageSystolic(Double previousAverageSystolic) {
        this.previousAverageSystolic = previousAverageSystolic;
    }

    public Double getPreviousAverageDiastolic() {
        return previousAverageDiastolic;
    }

    public void setPreviousAverageDiastolic(Double previousAverageDiastolic) {
        this.previousAverageDiastolic = previousAverageDiastolic;
    }

    public Double getSystolicPercentageChange() {
        return systolicPercentageChange;
    }

    public void setSystolicPercentageChange(Double systolicPercentageChange) {
        this.systolicPercentageChange = systolicPercentageChange;
    }

    public Double getDiastolicPercentageChange() {
        return diastolicPercentageChange;
    }

    public void setDiastolicPercentageChange(Double diastolicPercentageChange) {
        this.diastolicPercentageChange = diastolicPercentageChange;
    }
}
