package com.project.househealth.dashboard;

public interface DashboardService {
    DashboardResponse getDashboard();

    BloodPressureHistoryResponse getBloodPressureHistory();

    HealthMetricHistoryResponse getFastingSugarHistory();

    HealthMetricHistoryResponse getPostMealSugarHistory();
}
