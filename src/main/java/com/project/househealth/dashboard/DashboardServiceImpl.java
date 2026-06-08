package com.project.househealth.dashboard;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.service.HealthLogService;
import com.project.househealth.trendanalysis.BloodPressureTrendResponse;
import com.project.househealth.trendanalysis.SugarTrendResponse;
import com.project.househealth.trendanalysis.TrendAnalysisService;
import com.project.househealth.dto.response.BloodPressureLogResponse;
import com.project.househealth.dto.response.BloodSugarLogResponse;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final HealthLogService healthLogService;
    private final TrendAnalysisService trendAnalysisService;

    public DashboardServiceImpl(
            HealthLogService healthLogService,
            TrendAnalysisService trendAnalysisService
    ) {
        this.healthLogService = healthLogService;
        this.trendAnalysisService = trendAnalysisService;
    }

    @Override
    public DashboardResponse getDashboard() {

        DashboardResponse response =
                new DashboardResponse();

        HealthLog latestBp =
                healthLogService.getLatestBloodPressure();

        HealthLog latestFastingSugar =
                healthLogService.getLatestFastingSugar();

        BloodPressureTrendResponse bpTrend =
                trendAnalysisService
                        .analyzeBloodPressureTrend(
                                TrendPeriod.MONTH
                        );

        SugarTrendResponse fastingSugarTrend =
                trendAnalysisService
                        .analyzeFastingSugarTrend(
                                TrendPeriod.MONTH
                        );

        BloodPressureLogResponse bpResponse =
                new BloodPressureLogResponse();

        bpResponse.setLogId(latestBp.getLogId());
        bpResponse.setUserId(latestBp.getUser().getUserId());
        bpResponse.setSystolic(latestBp.getSystolic());
        bpResponse.setDiastolic(latestBp.getDiastolic());
        bpResponse.setLoggedAt(latestBp.getLoggedAt());

        BloodSugarLogResponse sugarResponse =
                new BloodSugarLogResponse();

        sugarResponse.setLogId(
                latestFastingSugar.getLogId()
        );
        sugarResponse.setUserId(
                latestFastingSugar.getUser().getUserId()
        );
        sugarResponse.setSugarValue(
                latestFastingSugar.getSugarValue()
        );
        sugarResponse.setSugarType(
                latestFastingSugar.getSugarType()
        );
        sugarResponse.setLoggedAt(
                latestFastingSugar.getLoggedAt()
        );

        response.setLatestBloodPressure(
                bpResponse
        );

        response.setLatestFastingSugar(
                sugarResponse
        );

        response.setBloodPressureTrend(
                bpTrend
        );

        response.setFastingSugarTrend(
                fastingSugarTrend
        );

        return response;
    }
}