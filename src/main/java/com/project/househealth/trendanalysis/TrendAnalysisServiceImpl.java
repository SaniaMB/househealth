package com.project.househealth.trendanalysis;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.enums.TrendStatus;
import com.project.househealth.repositories.HealthLogRepository;
import com.project.househealth.service.CurrentUserService;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TrendAnalysisServiceImpl implements TrendAnalysisService {

   private final HealthLogRepository healthLogRepository;
   private final CurrentUserService currentUserService;


    public TrendAnalysisServiceImpl(HealthLogRepository healthLogRepository, CurrentUserService currentUserService) {
        this.healthLogRepository = healthLogRepository;
        this.currentUserService = currentUserService;
    }

    private int getPeriodDays(TrendPeriod trendPeriod){
        return switch (trendPeriod) {
            case WEEK -> 7;
            case MONTH -> 30;
            case YEAR -> 365;
        };
    }

    private double calculatePercentageChange(double previousAverage, double currentAverage){

        if(previousAverage == 0){
            return 0;
        }

        return ((currentAverage - previousAverage) / previousAverage) * 100;
    }

    private TrendStatus determineTrendStatus(double percentageChange) {

        if (percentageChange <= -5) {
            return TrendStatus.IMPROVING;
        }

        if (percentageChange >= 5) {
            return TrendStatus.WORSENING;
        }

        return TrendStatus.STABLE;
    }

    private BloodPressureTrendResponse analyzeBloodPressureTrendInternal(
            Long userId,
            TrendPeriod trendPeriod
    ) {

        BloodPressureTrendResponse response = new BloodPressureTrendResponse();

        List<HealthLog> logs =
                healthLogRepository
                        .findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(
                                userId,
                                MetricType.BP
                        );

        if(logs.size() < 14){

            response.setTrendStatus(
                    TrendStatus.INSUFFICIENT_DATA
            );

            response.setTrendPeriod(trendPeriod);

            return response;

        }

        List<HealthLog> currentLogs =
                logs.subList(0, 7);

        List<HealthLog> previousLogs =
                logs.subList(7, 14);

        double currentAverageSystolic = currentLogs.stream()
                                        .mapToInt(HealthLog::getSystolic)
                                        .average()
                                        .orElse(0);

        double currentAverageDiastolic = currentLogs.stream()
                                        .mapToInt(HealthLog::getDiastolic)
                                        .average()
                                        .orElse(0);

        double previousAverageSystolic = previousLogs.stream()
                                        .mapToInt(HealthLog::getSystolic)
                                        .average()
                                        .orElse(0);

        double previousAverageDiastolic = previousLogs.stream()
                                          .mapToInt(HealthLog::getDiastolic)
                                          .average()
                                          .orElse(0);

        double systolicPercentageChange = calculatePercentageChange(
                                          previousAverageSystolic,
                                          currentAverageSystolic);

        double diastolicPercentageChange =
                                            calculatePercentageChange(
                                                    previousAverageDiastolic,
                                                    currentAverageDiastolic
                                            );

        TrendStatus systolicTrend =
                determineTrendStatus(systolicPercentageChange);

        TrendStatus diastolicTrend =
                determineTrendStatus(diastolicPercentageChange);

        TrendStatus overallTrend;

        if (systolicTrend == diastolicTrend) {
            overallTrend = systolicTrend;
        }
        else if (systolicTrend == TrendStatus.STABLE) {
            overallTrend = diastolicTrend;
        }
        else if (diastolicTrend == TrendStatus.STABLE) {
            overallTrend = systolicTrend;
        }
        else {
            overallTrend = TrendStatus.STABLE;
        }

        response.setTrendStatus(overallTrend);
        response.setTrendPeriod(trendPeriod);

        response.setCurrentAverageSystolic(currentAverageSystolic);
        response.setCurrentAverageDiastolic(currentAverageDiastolic);

        response.setPreviousAverageSystolic(previousAverageSystolic);
        response.setPreviousAverageDiastolic(previousAverageDiastolic);

        response.setSystolicPercentageChange(systolicPercentageChange);
        response.setDiastolicPercentageChange(diastolicPercentageChange);

        return response;
    }

    private SugarTrendResponse analyzeSugarTrendInternal(
            Long userId,
            TrendPeriod trendPeriod,
            SugarType sugarType
    ){

        SugarTrendResponse response = new SugarTrendResponse();

        List<HealthLog> logs =
                healthLogRepository
                        .findByUser_UserIdAndMetricTypeAndSugarTypeOrderByLoggedAtDesc(
                                userId,
                                MetricType.SUGAR,
                                sugarType
                        );

        if(logs.size() < 14){

            response.setTrendStatus(TrendStatus.INSUFFICIENT_DATA);

            response.setTrendPeriod(trendPeriod);

            response.setSugarType(sugarType);

            return response;
        }

        List<HealthLog> currentLogs =
                logs.subList(0, 7);

        List<HealthLog> previousLogs =
                logs.subList(7, 14);

        double currentAverageSugar = currentLogs.stream()
                                        .mapToInt(
                                                HealthLog::getSugarValue
                                        )
                                        .average()
                                        .orElse(0);

        double previousAverageSugar = previousLogs.stream()
                                        .mapToInt(
                                                HealthLog::getSugarValue
                                        )
                                        .average()
                                        .orElse(0);

        double percentageChange = calculatePercentageChange(previousAverageSugar, currentAverageSugar);

        TrendStatus trendStatus = determineTrendStatus(percentageChange);

        response.setTrendStatus(trendStatus);
        response.setTrendPeriod(trendPeriod);
        response.setSugarType(sugarType);
        response.setCurrentAverageSugar(currentAverageSugar);
        response.setPreviousAverageSugar(previousAverageSugar);
        response.setPercentageChange(percentageChange);

        return response;
    }

    @Override
    public BloodPressureTrendResponse analyzeBloodPressureTrend(
            TrendPeriod trendPeriod
    ) {

        return analyzeBloodPressureTrendInternal(
                currentUserService.getCurrentUserId(),
                trendPeriod
        );
    }

    @Override
    public BloodPressureTrendResponse analyzeBloodPressureTrend(
            Long userId,
            TrendPeriod trendPeriod
    ) {
        return analyzeBloodPressureTrendInternal(
                userId,
                trendPeriod
        );
    }

    @Override
    public SugarTrendResponse analyzeFastingSugarTrend(
            TrendPeriod trendPeriod
    ) {
        return analyzeSugarTrendInternal(
                currentUserService.getCurrentUserId(),
                trendPeriod,
                SugarType.FASTING
        );
    }

    @Override
    public SugarTrendResponse analyzePostMealSugarTrend(
            TrendPeriod trendPeriod
    ) {
        return analyzeSugarTrendInternal(
                currentUserService.getCurrentUserId(),
                trendPeriod,
                SugarType.POST_MEAL
        );
    }

    @Override
    public SugarTrendResponse analyzeFastingSugarTrend(
            Long userId,
            TrendPeriod trendPeriod
    ) {
        return analyzeSugarTrendInternal(
                userId,
                trendPeriod,
                SugarType.FASTING
        );
    }

    @Override
    public SugarTrendResponse analyzePostMealSugarTrend(
            Long userId,
            TrendPeriod trendPeriod
    ) {
        return analyzeSugarTrendInternal(
                userId,
                trendPeriod,
                SugarType.POST_MEAL
        );
    }

}
