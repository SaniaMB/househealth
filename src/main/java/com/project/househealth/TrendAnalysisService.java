package com.project.househealth;

import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.trendanalysis.BloodPressureTrendResponse;
import com.project.househealth.trendanalysis.SugarTrendResponse;

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
}
