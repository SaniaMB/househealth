package com.project.househealth.trendanalysis;

import com.project.househealth.enums.SugarType;
import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.enums.TrendStatus;

public class SugarTrendResponse {

    private TrendStatus trendStatus;
    private TrendPeriod trendPeriod;
    private SugarType sugarType;

    private Double currentAverageSugar;
    private Double previousAverageSugar;

    private Double percentageChange;

    public SugarTrendResponse() {}

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

    public SugarType getSugarType() {
        return sugarType;
    }

    public void setSugarType(SugarType sugarType) {
        this.sugarType = sugarType;
    }

    public Double getCurrentAverageSugar() {
        return currentAverageSugar;
    }

    public void setCurrentAverageSugar(Double currentAverageSugar) {
        this.currentAverageSugar = currentAverageSugar;
    }

    public Double getPreviousAverageSugar() {
        return previousAverageSugar;
    }

    public void setPreviousAverageSugar(Double previousAverageSugar) {
        this.previousAverageSugar = previousAverageSugar;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }
}
