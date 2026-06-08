package com.project.househealth.trendanalysis;

import com.project.househealth.enums.TrendPeriod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trends")
public class TrendAnalysisController {

    private final TrendAnalysisService trendAnalysisService;

    public TrendAnalysisController(TrendAnalysisService trendAnalysisService) {
        this.trendAnalysisService = trendAnalysisService;
    }

    @GetMapping("/blood-pressure")
    public ResponseEntity<BloodPressureTrendResponse> getBloodPressureTrend(
            @RequestParam TrendPeriod trendPeriod
    ){
        return ResponseEntity.ok(
                trendAnalysisService.analyzeBloodPressureTrend(trendPeriod)
        );
    }

    @GetMapping("/fasting-sugar")
    public ResponseEntity<SugarTrendResponse> getFastingSugarTrend(
            @RequestParam TrendPeriod trendPeriod
    ){
        return ResponseEntity.ok(
                trendAnalysisService.analyzeFastingSugarTrend(trendPeriod)
        );
    }

    @GetMapping("/post-meal-sugar")
    public ResponseEntity<SugarTrendResponse> getPostMealSugarTrend(
            @RequestParam TrendPeriod trendPeriod
    ){
        return ResponseEntity.ok(
                trendAnalysisService.analyzePostMealSugarTrend(trendPeriod)
        );
    }

}
