package com.project.househealth.reports;

import com.project.househealth.dashboard.BloodPressureHistoryResponse;
import com.project.househealth.dashboard.DashboardResponse;
import com.project.househealth.dashboard.HealthMetricHistoryResponse;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;

import java.util.List;

public class HealthReportData {

    private User user;

    private DashboardResponse dashboard;

    private BloodPressureHistoryResponse bloodPressureHistory;

    private HealthMetricHistoryResponse fastingSugarHistory;

    private HealthMetricHistoryResponse postMealSugarHistory;

    private List<HealthLog> recentBloodPressureLogs;

    private List<HealthLog> recentFastingSugarLogs;

    private List<HealthLog> recentPostMealSugarLogs;

    public List<HealthLog> getRecentBloodPressureLogs() {
        return recentBloodPressureLogs;
    }

    public void setRecentBloodPressureLogs(List<HealthLog> recentBloodPressureLogs) {
        this.recentBloodPressureLogs = recentBloodPressureLogs;
    }

    public List<HealthLog> getRecentFastingSugarLogs() {
        return recentFastingSugarLogs;
    }

    public void setRecentFastingSugarLogs(List<HealthLog> recentFastingSugarLogs) {
        this.recentFastingSugarLogs = recentFastingSugarLogs;
    }

    public List<HealthLog> getRecentPostMealSugarLogs() {
        return recentPostMealSugarLogs;
    }

    public void setRecentPostMealSugarLogs(List<HealthLog> recentPostMealSugarLogs) {
        this.recentPostMealSugarLogs = recentPostMealSugarLogs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DashboardResponse getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardResponse dashboard) {
        this.dashboard = dashboard;
    }

    public BloodPressureHistoryResponse getBloodPressureHistory() {
        return bloodPressureHistory;
    }

    public void setBloodPressureHistory(
            BloodPressureHistoryResponse bloodPressureHistory
    ) {
        this.bloodPressureHistory = bloodPressureHistory;
    }

    public HealthMetricHistoryResponse getFastingSugarHistory() {
        return fastingSugarHistory;
    }

    public void setFastingSugarHistory(
            HealthMetricHistoryResponse fastingSugarHistory
    ) {
        this.fastingSugarHistory = fastingSugarHistory;
    }

    public HealthMetricHistoryResponse getPostMealSugarHistory() {
        return postMealSugarHistory;
    }

    public void setPostMealSugarHistory(
            HealthMetricHistoryResponse postMealSugarHistory
    ) {
        this.postMealSugarHistory = postMealSugarHistory;
    }
}