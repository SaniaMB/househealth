package com.project.househealth.controllers;

import com.project.househealth.dto.request.CreateBloodPressureLogRequest;
import com.project.househealth.dto.request.CreateBloodSugarLogRequest;
import com.project.househealth.dto.response.BloodPressureLogResponse;
import com.project.househealth.dto.response.BloodSugarLogResponse;
import com.project.househealth.dto.response.FeedItemResponse;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.MetricType;
import com.project.househealth.service.HealthLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/healthlogs")
public class HealthLogController {

    private final HealthLogService healthLogService;

    public HealthLogController(HealthLogService healthLogService) {
        this.healthLogService = healthLogService;
    }

    @PostMapping("/blood-pressure")
    public ResponseEntity<BloodPressureLogResponse> recordBloodPressure(
            @Valid @RequestBody CreateBloodPressureLogRequest request
    ) {

        HealthLog healthLog =
                healthLogService.recordBloodPressure(
                        request.getSystolic(),
                        request.getDiastolic()
                );

        BloodPressureLogResponse response =
                new BloodPressureLogResponse();

        response.setLogId(healthLog.getLogId());
        response.setUserId(healthLog.getUser().getUserId());
        response.setSystolic(healthLog.getSystolic());
        response.setDiastolic(healthLog.getDiastolic());
        response.setLoggedAt(healthLog.getLoggedAt());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/blood-sugar")
    public ResponseEntity<BloodSugarLogResponse> recordBloodSugar(
            @Valid @RequestBody CreateBloodSugarLogRequest request
    ) {

        HealthLog healthLog =
                healthLogService.recordBloodSugar(
                        request.getSugarValue(),
                        request.getSugarType()
                );

        BloodSugarLogResponse response =
                new BloodSugarLogResponse();

        response.setLogId(healthLog.getLogId());
        response.setUserId(healthLog.getUser().getUserId());
        response.setSugarValue(healthLog.getSugarValue());
        response.setSugarType(healthLog.getSugarType());
        response.setLoggedAt(healthLog.getLoggedAt());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{logId}")
    public ResponseEntity<Object> getHealthLogById(
            @PathVariable Long logId
    ) {

        HealthLog healthLog =
                healthLogService.getHealthLogById(logId);

        if (healthLog.getMetricType() == MetricType.BP) {

            BloodPressureLogResponse response =
                    new BloodPressureLogResponse();

            response.setLogId(healthLog.getLogId());
            response.setUserId(healthLog.getUser().getUserId());
            response.setSystolic(healthLog.getSystolic());
            response.setDiastolic(healthLog.getDiastolic());
            response.setLoggedAt(healthLog.getLoggedAt());

            return ResponseEntity.ok(response);
        }

        BloodSugarLogResponse response =
                new BloodSugarLogResponse();

        response.setLogId(healthLog.getLogId());
        response.setUserId(healthLog.getUser().getUserId());
        response.setSugarValue(healthLog.getSugarValue());
        response.setSugarType(healthLog.getSugarType());
        response.setLoggedAt(healthLog.getLoggedAt());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-logs")
    public ResponseEntity<List<Object>> getMyLogs(
    ) {

        List<HealthLog> logs =
                healthLogService.getMyLogs();

        List<Object> responses = logs.stream()
                .map(log -> {

                    if (log.getMetricType() == MetricType.BP) {

                        BloodPressureLogResponse response =
                                new BloodPressureLogResponse();

                        response.setLogId(log.getLogId());
                        response.setUserId(log.getUser().getUserId());
                        response.setSystolic(log.getSystolic());
                        response.setDiastolic(log.getDiastolic());
                        response.setLoggedAt(log.getLoggedAt());

                        return response;
                    }

                    BloodSugarLogResponse response =
                            new BloodSugarLogResponse();

                    response.setLogId(log.getLogId());
                    response.setUserId(log.getUser().getUserId());
                    response.setSugarValue(log.getSugarValue());
                    response.setSugarType(log.getSugarType());
                    response.setLoggedAt(log.getLoggedAt());

                    return response;
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my-logs/metric/{metricType}")
    public ResponseEntity<List<Object>> getLogsByMetric(
            @PathVariable MetricType metricType
    ) {

        List<HealthLog> logs =
                healthLogService.getLogsByMetric(
                        metricType
                );

        List<Object> responses = logs.stream()
                .map(log -> {

                    if (log.getMetricType() == MetricType.BP) {

                        BloodPressureLogResponse response =
                                new BloodPressureLogResponse();

                        response.setLogId(log.getLogId());
                        response.setUserId(log.getUser().getUserId());
                        response.setSystolic(log.getSystolic());
                        response.setDiastolic(log.getDiastolic());
                        response.setLoggedAt(log.getLoggedAt());

                        return response;
                    }

                    BloodSugarLogResponse response =
                            new BloodSugarLogResponse();

                    response.setLogId(log.getLogId());
                    response.setUserId(log.getUser().getUserId());
                    response.setSugarValue(log.getSugarValue());
                    response.setSugarType(log.getSugarType());
                    response.setLoggedAt(log.getLoggedAt());

                    return response;
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<FeedItemResponse>> getFamilyFeed(
            @PathVariable Long familyId
    ) {

        List<HealthLog> feed = healthLogService.getFamilyFeed(familyId);

        List<FeedItemResponse> responses = feed.stream()
                                            .map(log -> {

                                                FeedItemResponse response = new FeedItemResponse();

                                                response.setUserId(log.getUser().getUserId());
                                                response.setUserName(log.getUser().getName());
                                                response.setMetricType(log.getMetricType());
                                                response.setSystolic(log.getSystolic());
                                                response.setDiastolic(log.getDiastolic());
                                                response.setSugarValue(log.getSugarValue());
                                                response.setSugarType(log.getSugarType());
                                                response.setLoggedAt(log.getLoggedAt());

                                                return response;
                                            })
                                            .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/latest-blood-pressure")
    public ResponseEntity<BloodPressureLogResponse> getLatestBloodPressure() {

        HealthLog healthLog =
                healthLogService.getLatestBloodPressure();

        BloodPressureLogResponse response =
                new BloodPressureLogResponse();

        response.setLogId(healthLog.getLogId());
        response.setUserId(healthLog.getUser().getUserId());
        response.setSystolic(healthLog.getSystolic());
        response.setDiastolic(healthLog.getDiastolic());
        response.setLoggedAt(healthLog.getLoggedAt());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest-fasting-sugar")
    public ResponseEntity<BloodSugarLogResponse> getLatestFastingSugar() {

        HealthLog healthLog =
                healthLogService.getLatestFastingSugar();

        BloodSugarLogResponse response =
                new BloodSugarLogResponse();

        response.setLogId(healthLog.getLogId());
        response.setUserId(healthLog.getUser().getUserId());
        response.setSugarValue(healthLog.getSugarValue());
        response.setSugarType(healthLog.getSugarType());
        response.setLoggedAt(healthLog.getLoggedAt());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest-post-meal-sugar")
    public ResponseEntity<BloodSugarLogResponse> getLatestPostMealSugar() {

        HealthLog healthLog =
                healthLogService.getLatestPostMealSugar();

        BloodSugarLogResponse response =
                new BloodSugarLogResponse();

        response.setLogId(healthLog.getLogId());
        response.setUserId(healthLog.getUser().getUserId());
        response.setSugarValue(healthLog.getSugarValue());
        response.setSugarType(healthLog.getSugarType());
        response.setLoggedAt(healthLog.getLoggedAt());

        return ResponseEntity.ok(response);
    }
}