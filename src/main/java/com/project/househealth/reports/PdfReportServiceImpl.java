package com.project.househealth.reports;

import com.project.househealth.dashboard.BloodPressureHistoryResponse;
import com.project.househealth.dashboard.DashboardResponse;
import com.project.househealth.dashboard.DashboardService;
import com.project.househealth.dashboard.HealthMetricHistoryResponse;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import com.project.househealth.repositories.HealthLogRepository;
import com.project.househealth.service.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfReportServiceImpl implements PdfReportService {

    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;
    private final PdfGenerator pdfGenerator;
    private final HealthLogRepository healthLogRepository;

    public PdfReportServiceImpl(
            DashboardService dashboardService,
            CurrentUserService currentUserService, PdfGenerator pdfGenerator, HealthLogRepository healthLogRepository
    ) {
        this.dashboardService = dashboardService;
        this.currentUserService = currentUserService;
        this.pdfGenerator = pdfGenerator;
        this.healthLogRepository = healthLogRepository;
    }

    @Override
    public byte[] generateHealthReport() {

        User user = currentUserService.getCurrentUser();

        DashboardResponse dashboard =
                dashboardService.getDashboard();

        BloodPressureHistoryResponse bloodPressureHistory =
                dashboardService.getBloodPressureHistory();

        HealthMetricHistoryResponse fastingSugarHistory =
                dashboardService.getFastingSugarHistory();

        HealthMetricHistoryResponse postMealSugarHistory =
                dashboardService.getPostMealSugarHistory();

        List<HealthLog> recentBloodPressureLogs =
                healthLogRepository
                        .findTop7ByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(
                                user.getUserId(),
                                MetricType.BP
                        );

        List<HealthLog> recentFastingSugarLogs =
                healthLogRepository
                        .findTop7ByUser_UserIdAndMetricTypeAndSugarTypeOrderByLoggedAtDesc(
                                user.getUserId(),
                                MetricType.SUGAR,
                                SugarType.FASTING
                        );

        List<HealthLog> recentPostMealSugarLogs =
                healthLogRepository
                        .findTop7ByUser_UserIdAndMetricTypeAndSugarTypeOrderByLoggedAtDesc(
                                user.getUserId(),
                                MetricType.SUGAR,
                                SugarType.POST_MEAL
                        );

        HealthReportData reportData =
                new HealthReportData();

        reportData.setUser(user);

        reportData.setDashboard(dashboard);

        reportData.setBloodPressureHistory(
                bloodPressureHistory
        );

        reportData.setFastingSugarHistory(
                fastingSugarHistory
        );

        reportData.setPostMealSugarHistory(
                postMealSugarHistory
        );

        reportData.setRecentBloodPressureLogs(
                recentBloodPressureLogs
        );

        reportData.setRecentFastingSugarLogs(
                recentFastingSugarLogs
        );

        reportData.setRecentPostMealSugarLogs(
                recentPostMealSugarLogs
        );

        // PDF generation comes in the next step.
        return pdfGenerator.generate(reportData);
    }
}