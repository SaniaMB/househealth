package com.project.househealth.trendanalysis;

import com.project.househealth.enums.TrendPeriod;

public interface TrendAnalysisService {

    BloodPressureTrendResponse analyzeBloodPressureTrend(
            TrendPeriod trendPeriod
    );

    SugarTrendResponse analyzeFastingSugarTrend(
            TrendPeriod trendPeriod
    );

    SugarTrendResponse analyzePostMealSugarTrend(
            TrendPeriod trendPeriod
    );

    BloodPressureTrendResponse analyzeBloodPressureTrend(
            Long userId,
            TrendPeriod trendPeriod
    );

    SugarTrendResponse analyzeFastingSugarTrend(
            Long userId,
            TrendPeriod trendPeriod
    );

    SugarTrendResponse analyzePostMealSugarTrend(
            Long userId,
            TrendPeriod trendPeriod
    );
}