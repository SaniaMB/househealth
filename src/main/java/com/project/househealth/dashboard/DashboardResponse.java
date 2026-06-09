package com.project.househealth.dashboard;

import com.project.househealth.dto.response.BloodPressureLogResponse;
import com.project.househealth.dto.response.BloodSugarLogResponse;
import com.project.househealth.trendanalysis.BloodPressureTrendResponse;
import com.project.househealth.trendanalysis.SugarTrendResponse;

public class DashboardResponse {
    private BloodPressureLogResponse latestBloodPressure;

    private BloodSugarLogResponse latestFastingSugar;

    private BloodPressureTrendResponse bloodPressureTrend;

    private SugarTrendResponse fastingSugarTrend;

    private BloodSugarLogResponse latestPostMealSugar;

    private SugarTrendResponse postMealSugarTrend;

    public BloodPressureLogResponse getLatestBloodPressure() {
        return latestBloodPressure;
    }

    public void setLatestBloodPressure(BloodPressureLogResponse latestBloodPressure) {
        this.latestBloodPressure = latestBloodPressure;
    }

    public BloodSugarLogResponse getLatestFastingSugar() {
        return latestFastingSugar;
    }

    public void setLatestFastingSugar(BloodSugarLogResponse latestFastingSugar) {
        this.latestFastingSugar = latestFastingSugar;
    }

    public BloodPressureTrendResponse getBloodPressureTrend() {
        return bloodPressureTrend;
    }

    public void setBloodPressureTrend(BloodPressureTrendResponse bloodPressureTrend) {
        this.bloodPressureTrend = bloodPressureTrend;
    }

    public SugarTrendResponse getFastingSugarTrend() {
        return fastingSugarTrend;
    }

    public void setFastingSugarTrend(SugarTrendResponse fastingSugarTrend) {
        this.fastingSugarTrend = fastingSugarTrend;
    }

    public BloodSugarLogResponse getLatestPostMealSugar() {
        return latestPostMealSugar;
    }

    public void setLatestPostMealSugar(BloodSugarLogResponse latestPostMealSugar) {
        this.latestPostMealSugar = latestPostMealSugar;
    }

    public SugarTrendResponse getPostMealSugarTrend() {
        return postMealSugarTrend;
    }

    public void setPostMealSugarTrend(SugarTrendResponse postMealSugarTrend) {
        this.postMealSugarTrend = postMealSugarTrend;
    }
}
