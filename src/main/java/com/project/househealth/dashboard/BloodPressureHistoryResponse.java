package com.project.househealth.dashboard;

import java.util.List;

public class BloodPressureHistoryResponse {

    private List<BloodPressureHistoryPointResponse> points;

    public List<BloodPressureHistoryPointResponse> getPoints() {
        return points;
    }

    public void setPoints(
            List<BloodPressureHistoryPointResponse> points
    ) {
        this.points = points;
    }
}