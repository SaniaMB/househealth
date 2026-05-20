package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import com.project.househealth.exception.FamilyNotFoundException;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.FamilyRepository;
import com.project.househealth.repositories.HealthLogRepository;
import com.project.househealth.exception.InvalidHealthLogException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HealthLogServiceImpl implements HealthLogService{

    private final HealthLogRepository healthLogRepository;
    private final UserService userService;
    private final FamilyMembershipRepository familyMembershipRepository;


    public HealthLogServiceImpl(HealthLogRepository healthLogRepository, UserService userService, FamilyService familyService, FamilyRepository familyRepository, FamilyMembershipRepository familyMembershipRepository){
        this.healthLogRepository = healthLogRepository;
        this.userService = userService;
        this.familyMembershipRepository = familyMembershipRepository;
    }

    private void validateBloodPressure(Integer systolic,Integer diastolic){
        if(systolic == null) throw new InvalidHealthLogException("Systolic value cannot be null");
        if(diastolic == null) throw new InvalidHealthLogException("Diastolic value cannot be null");

        if(systolic < 70 || systolic > 250) throw new InvalidHealthLogException("Systolic pressure must be between 70 and 250");
        if(diastolic < 40 || diastolic > 150) throw new InvalidHealthLogException("Diastolic pressure must be between 40 and 150");

        if (systolic <= diastolic) throw new InvalidHealthLogException("Systolic pressure must be greater than diastolic pressure");
    }

    @Override
    public HealthLog recordBloodPressure(Long actingUserId,
                               Integer systolic,
                               Integer diastolic){


        User actingUser = userService.getUserById(actingUserId);

        validateBloodPressure(systolic, diastolic);

        HealthLog healthLog = new HealthLog(MetricType.BP, actingUser);

        healthLog.setSystolic(systolic);
        healthLog.setDiastolic(diastolic);

        return healthLogRepository.save(healthLog);
    }

    private void validateBloodSugar(Integer sugarValue,
                                    SugarType sugarType){
        if(sugarValue == null) throw new InvalidHealthLogException("Sugar value cannot be null");
        if(sugarType == null) throw new InvalidHealthLogException("Sugar Type cannot be null");

        if (sugarValue < 40 || sugarValue > 700)
            throw new InvalidHealthLogException("Sugar Value must be between 40 and 700");
    }

    @Override
    public HealthLog recordBloodSugar(Long actingUserId,
                               Integer sugarValue,
                               SugarType sugarType){

        User actingUser = userService.getUserById(actingUserId);

        validateBloodSugar(sugarValue, sugarType);

        HealthLog healthLog = new HealthLog(MetricType.SUGAR, actingUser);

        healthLog.setSugarValue(sugarValue);
        healthLog.setSugarType(sugarType);

        return healthLogRepository.save(healthLog);
    }


    @Override
    public HealthLog getHealthLogById(Long id) {

        return healthLogRepository.findById(id)
                .orElseThrow(() -> new InvalidHealthLogException("Health log not found"));
    }

    @Override
    public List<HealthLog> getMyLogs(Long actingUserId){
        userService.getUserById(actingUserId);

        List<HealthLog> healthLogs = healthLogRepository
                                     .findByUser_UserIdOrderByLoggedAtDesc(actingUserId);

        return healthLogs;
    }

    @Override
    public List<HealthLog> getLogsByMetric(Long actingUserId,
                                    MetricType metricType){

        List<HealthLog> healthLogsByMetric = healthLogRepository
                                    .findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(actingUserId, metricType);

        return healthLogsByMetric;
    }

    @Override
    public List<HealthLog> getFamilyFeed(Long familyId, Long actingUserId){

        userService.getUserById(actingUserId);

        FamilyMembership familyMembership =
                familyMembershipRepository
                        .findByUser_UserIdAndFamily_FamilyId(actingUserId, familyId)
                        .orElseThrow(() ->
                                new MembershipNotFoundException(
                                        "User does not belong to this family"));

        Family family = familyMembership.getFamily();

        List<FamilyMembership> familyMemberships =
                family.getFamilyMemberships();

        List<User> familyMembers = new ArrayList<>();

        for (FamilyMembership membership : familyMemberships) {
            familyMembers.add(membership.getUser());
        }

        return healthLogRepository
                .findByUserInOrderByLoggedAtDesc(familyMembers);
    }
}
