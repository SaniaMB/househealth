package com.project.househealth.controllers;

import com.project.househealth.dto.request.CreateBloodPressureLogRequest;
import com.project.househealth.dto.request.CreateBloodSugarLogRequest;
import com.project.househealth.dto.response.BloodPressureLogResponse;
import com.project.househealth.dto.response.BloodSugarLogResponse;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.enums.MetricType;
import com.project.househealth.service.HealthLogService;
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
            @RequestBody CreateBloodPressureLogRequest request
    ) {

        HealthLog healthLog =
                healthLogService.recordBloodPressure(
                        request.getActingUserId(),
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
            @RequestBody CreateBloodSugarLogRequest request
    ) {

        HealthLog healthLog =
                healthLogService.recordBloodSugar(
                        request.getActingUserId(),
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

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Object>> getMyLogs(
            @PathVariable Long userId
    ) {

        List<HealthLog> logs =
                healthLogService.getMyLogs(userId);

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

    @GetMapping("/users/{userId}/metric/{metricType}")
    public ResponseEntity<List<Object>> getLogsByMetric(
            @PathVariable Long userId,
            @PathVariable MetricType metricType
    ) {

        List<HealthLog> logs =
                healthLogService.getLogsByMetric(
                        userId,
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

    @GetMapping("/family/{familyId}/user/{actingUserId}")
    public ResponseEntity<List<Object>> getFamilyFeed(
            @PathVariable Long familyId,
            @PathVariable Long actingUserId
    ) {

        List<HealthLog> feed =
                healthLogService.getFamilyFeed(
                        familyId,
                        actingUserId
                );

        List<Object> responses = feed.stream()
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
}