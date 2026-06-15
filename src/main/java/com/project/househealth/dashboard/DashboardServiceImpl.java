package com.project.househealth.dashboard;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.exception.InvalidHealthLogException;
import com.project.househealth.repositories.HealthLogRepository;
import com.project.househealth.service.CurrentUserService;
import com.project.househealth.service.HealthLogService;
import com.project.househealth.trendanalysis.BloodPressureTrendResponse;
import com.project.househealth.trendanalysis.SugarTrendResponse;
import com.project.househealth.trendanalysis.TrendAnalysisService;
import com.project.househealth.dto.response.BloodPressureLogResponse;
import com.project.househealth.dto.response.BloodSugarLogResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final HealthLogService healthLogService;
    private final TrendAnalysisService trendAnalysisService;
    private final HealthLogRepository healthLogRepository;
    private final CurrentUserService currentUserService;

    public DashboardServiceImpl(
            HealthLogService healthLogService,
            TrendAnalysisService trendAnalysisService, HealthLogRepository healthLogRepository, CurrentUserService currentUserService
    ) {
        this.healthLogService = healthLogService;
        this.trendAnalysisService = trendAnalysisService;
        this.healthLogRepository = healthLogRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public DashboardResponse getDashboard() {

        DashboardResponse response =
                new DashboardResponse();

        HealthLog latestBp = null;
        HealthLog latestFastingSugar = null;
        HealthLog latestPostMealSugar = null;

        try {
            latestBp = healthLogService.getLatestBloodPressure();
        } catch (InvalidHealthLogException ignored) {
        }

        try {
            latestFastingSugar = healthLogService.getLatestFastingSugar();
        } catch (InvalidHealthLogException ignored) {
        }

        try {
            latestPostMealSugar = healthLogService.getLatestPostMealSugar();
        } catch (InvalidHealthLogException ignored) {
        }

        BloodPressureTrendResponse bpTrend =
                trendAnalysisService
                        .analyzeBloodPressureTrend(
                                TrendPeriod.WEEK
                        );

        SugarTrendResponse fastingSugarTrend =
                trendAnalysisService
                        .analyzeFastingSugarTrend(
                                TrendPeriod.WEEK
                        );

        SugarTrendResponse postMealSugarTrend =
                trendAnalysisService
                        .analyzePostMealSugarTrend(
                                TrendPeriod.WEEK
                        );

        BloodPressureLogResponse bpResponse = null;

        if (latestBp != null) {

            bpResponse = new BloodPressureLogResponse();

            bpResponse.setLogId(
                    latestBp.getLogId()
            );

            bpResponse.setUserId(
                    latestBp.getUser().getUserId()
            );

            bpResponse.setSystolic(
                    latestBp.getSystolic()
            );

            bpResponse.setDiastolic(
                    latestBp.getDiastolic()
            );

            bpResponse.setLoggedAt(
                    latestBp.getLoggedAt()
            );
        }

        BloodSugarLogResponse fastingSugarResponse = null;

        if (latestFastingSugar != null) {

            fastingSugarResponse =
                    new BloodSugarLogResponse();

            fastingSugarResponse.setLogId(
                    latestFastingSugar.getLogId()
            );

            fastingSugarResponse.setUserId(
                    latestFastingSugar.getUser().getUserId()
            );

            fastingSugarResponse.setSugarValue(
                    latestFastingSugar.getSugarValue()
            );

            fastingSugarResponse.setSugarType(
                    latestFastingSugar.getSugarType()
            );

            fastingSugarResponse.setLoggedAt(
                    latestFastingSugar.getLoggedAt()
            );
        }

        BloodSugarLogResponse postMealSugarResponse = null;

        if (latestPostMealSugar != null) {

            postMealSugarResponse =
                    new BloodSugarLogResponse();

            postMealSugarResponse.setLogId(
                    latestPostMealSugar.getLogId()
            );

            postMealSugarResponse.setUserId(
                    latestPostMealSugar.getUser().getUserId()
            );

            postMealSugarResponse.setSugarValue(
                    latestPostMealSugar.getSugarValue()
            );

            postMealSugarResponse.setSugarType(
                    latestPostMealSugar.getSugarType()
            );

            postMealSugarResponse.setLoggedAt(
                    latestPostMealSugar.getLoggedAt()
            );
        }

        response.setLatestBloodPressure(
                bpResponse
        );

        response.setLatestFastingSugar(
                fastingSugarResponse
        );

        response.setLatestPostMealSugar(
                postMealSugarResponse
        );

        response.setBloodPressureTrend(
                bpTrend
        );

        response.setFastingSugarTrend(
                fastingSugarTrend
        );

        response.setPostMealSugarTrend(
                postMealSugarTrend
        );

        return response;
    }

    @Override
    public BloodPressureHistoryResponse
    getBloodPressureHistory() {

        Long userId =
                currentUserService.getCurrentUserId();

        List<HealthLog> logs =
                healthLogRepository
                        .findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(
                                userId,
                                MetricType.BP
                        );

        Map<LocalDate, List<HealthLog>> logsByDate =
                new LinkedHashMap<>();

        for (HealthLog log : logs) {

            LocalDate date =
                    log.getLoggedAt()
                            .atZone(
                                    ZoneId.systemDefault()
                            )
                            .toLocalDate();

            logsByDate
                    .computeIfAbsent(
                            date,
                            d -> new ArrayList<>()
                    )
                    .add(log);
        }

        List<BloodPressureHistoryPointResponse> points =
                new ArrayList<>();

        for (Map.Entry<LocalDate, List<HealthLog>> entry
                : logsByDate.entrySet()) {

            double averageSystolic =
                    entry.getValue()
                            .stream()
                            .mapToInt(
                                    HealthLog::getSystolic
                            )
                            .average()
                            .orElse(0);

            double averageDiastolic =
                    entry.getValue()
                            .stream()
                            .mapToInt(
                                    HealthLog::getDiastolic
                            )
                            .average()
                            .orElse(0);

            BloodPressureHistoryPointResponse point =
                    new BloodPressureHistoryPointResponse();

            point.setDate(
                    entry.getKey()
            );

            point.setSystolic(
                    averageSystolic
            );

            point.setDiastolic(
                    averageDiastolic
            );

            points.add(point);
        }

        BloodPressureHistoryResponse response =
                new BloodPressureHistoryResponse();

        response.setPoints(points);

        return response;
    }

    @Override
    public HealthMetricHistoryResponse
    getFastingSugarHistory() {

        return getSugarHistory(
                SugarType.FASTING
        );
    }

    @Override
    public HealthMetricHistoryResponse
    getPostMealSugarHistory() {

        return getSugarHistory(
                SugarType.POST_MEAL
        );
    }

    private HealthMetricHistoryResponse
    getSugarHistory(
            SugarType sugarType
    ) {

        Long userId =
                currentUserService.getCurrentUserId();

        List<HealthLog> logs =
                healthLogRepository
                        .findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(
                                userId,
                                MetricType.SUGAR
                        );

        Map<LocalDate, List<HealthLog>> logsByDate =
                new LinkedHashMap<>();

        for (HealthLog log : logs) {

            if (log.getSugarType() != sugarType) {
                continue;
            }

            LocalDate date =
                    log.getLoggedAt()
                            .atZone(
                                    ZoneId.systemDefault()
                            )
                            .toLocalDate();

            logsByDate
                    .computeIfAbsent(
                            date,
                            d -> new ArrayList<>()
                    )
                    .add(log);
        }

        List<TrendDataPointResponse> points =
                new ArrayList<>();

        for (Map.Entry<LocalDate, List<HealthLog>> entry
                : logsByDate.entrySet()) {

            double averageSugar =
                    entry.getValue()
                            .stream()
                            .mapToInt(
                                    HealthLog::getSugarValue
                            )
                            .average()
                            .orElse(0);

            TrendDataPointResponse point =
                    new TrendDataPointResponse();

            point.setDate(
                    entry.getKey()
            );

            point.setValue(
                    averageSugar
            );

            points.add(point);
        }

        HealthMetricHistoryResponse response =
                new HealthMetricHistoryResponse();

        response.setPoints(points);

        return response;
    }


}