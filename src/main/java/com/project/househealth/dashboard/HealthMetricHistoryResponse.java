package com.project.househealth.dashboard;

import java.util.List;

public class HealthMetricHistoryResponse {

    private List<TrendDataPointResponse> points;

    public List<TrendDataPointResponse> getPoints() {
        return points;
    }

    public void setPoints(
            List<TrendDataPointResponse> points
    ) {
        this.points = points;
    }
}