package com.project.househealth.service;

import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.User;
import com.project.househealth.enums.MetricType;
import com.project.househealth.enums.SugarType;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.HealthLogRepository;
import com.project.househealth.exception.InvalidHealthLogException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class HealthLogServiceImpl implements HealthLogService{

    private final HealthLogRepository healthLogRepository;
    private final FamilyMembershipRepository familyMembershipRepository;
    private final CurrentUserService currentUserService;


    public HealthLogServiceImpl(HealthLogRepository healthLogRepository, FamilyMembershipRepository familyMembershipRepository, CurrentUserService currentUserService){
        this.healthLogRepository = healthLogRepository;
        this.familyMembershipRepository = familyMembershipRepository;
        this.currentUserService = currentUserService;
    }

    private void validateBloodPressure(Integer systolic,Integer diastolic){
        if(systolic == null) throw new InvalidHealthLogException("Systolic value cannot be null");
        if(diastolic == null) throw new InvalidHealthLogException("Diastolic value cannot be null");

        if(systolic < 70 || systolic > 250) throw new InvalidHealthLogException("Systolic pressure must be between 70 and 250");
        if(diastolic < 40 || diastolic > 150) throw new InvalidHealthLogException("Diastolic pressure must be between 40 and 150");

        if (systolic <= diastolic) throw new InvalidHealthLogException("Systolic pressure must be greater than diastolic pressure");
    }

    @Override
    public HealthLog recordBloodPressure(Integer systolic,Integer diastolic){


        User currentUser = currentUserService.getCurrentUser();

        validateBloodPressure(systolic, diastolic);

        HealthLog healthLog = new HealthLog(MetricType.BP, currentUser);

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
    public HealthLog recordBloodSugar(Integer sugarValue, SugarType sugarType){

        User currentUser = currentUserService.getCurrentUser();

        validateBloodSugar(sugarValue, sugarType);

        HealthLog healthLog = new HealthLog(MetricType.SUGAR, currentUser);

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
    public List<HealthLog> getMyLogs(){

        User currentUser = currentUserService.getCurrentUser();

        List<HealthLog> healthLogs = healthLogRepository
                                     .findByUser_UserIdOrderByLoggedAtDesc(currentUser.getUserId());

        return healthLogs;
    }

    @Override
    public List<HealthLog> getLogsByMetric(MetricType metricType){

        User currentUser = currentUserService.getCurrentUser();

        List<HealthLog> healthLogsByMetric = healthLogRepository
                                    .findByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(currentUser.getUserId(), metricType);

        return healthLogsByMetric;
    }

    @Override
    public List<HealthLog> getFamilyFeed(Long familyId) {

        User currentUser = currentUserService.getCurrentUser();

        familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(
                        currentUser.getUserId(),
                        familyId
                )
                .orElseThrow(() ->
                        new MembershipNotFoundException(
                                "User does not belong to this family"
                        ));

        List<FamilyMembership> familyMemberships =
                familyMembershipRepository
                        .findByFamily_FamilyId(familyId);

        List<User> familyMembers = new ArrayList<>();

        for (FamilyMembership membership : familyMemberships) {

            User member = membership.getUser();

            if (!member.getUserId().equals(currentUser.getUserId())) {
                familyMembers.add(member);
            }
        }

        return healthLogRepository
                .findByUserInOrderByLoggedAtDesc(
                        familyMembers
                );
    }

    @Override
    public HealthLog getLatestBloodPressure() {

        User currentUser = currentUserService.getCurrentUser();

        return healthLogRepository
                .findFirstByUser_UserIdAndMetricTypeOrderByLoggedAtDesc(
                        currentUser.getUserId(),
                        MetricType.BP
                )
                .orElseThrow(() ->
                        new InvalidHealthLogException(
                                "No blood pressure logs found"
                        ));
    }

    @Override
    public HealthLog getLatestFastingSugar() {

        User currentUser = currentUserService.getCurrentUser();

        return healthLogRepository
                .findFirstByUser_UserIdAndMetricTypeAndSugarTypeOrderByLoggedAtDesc(
                        currentUser.getUserId(),
                        MetricType.SUGAR,
                        SugarType.FASTING
                )
                .orElseThrow(() ->
                        new InvalidHealthLogException(
                                "No fasting sugar logs found"
                        ));
    }

    @Override
    public HealthLog getLatestPostMealSugar() {

        User currentUser = currentUserService.getCurrentUser();

        return healthLogRepository
                .findFirstByUser_UserIdAndMetricTypeAndSugarTypeOrderByLoggedAtDesc(
                        currentUser.getUserId(),
                        MetricType.SUGAR,
                        SugarType.POST_MEAL
                )
                .orElseThrow(() ->
                        new InvalidHealthLogException(
                                "No post meal sugar logs found"
                        ));
    }

    @Override
    public List<HealthLog> getMyFeed() {

        User currentUser = currentUserService.getCurrentUser();

        List<FamilyMembership> myMemberships = familyMembershipRepository.findByUser_UserId(currentUser.getUserId());

        List<Long> memberIds = new ArrayList<>();

        for (FamilyMembership myMembership : myMemberships) {

            Long familyId = myMembership.getFamily()
                    .getFamilyId();

            List<FamilyMembership> familyMemberships = familyMembershipRepository.findByFamily_FamilyId(familyId);

            for (FamilyMembership membership : familyMemberships) {

                Long userId = membership.getUser().getUserId();

                if (!userId.equals(currentUser.getUserId())) {

                    if (!memberIds.contains(userId)) {
                        memberIds.add(userId);
                    }
                }
            }
        }

        if (memberIds.isEmpty()) {
            return List.of();
        }

        return healthLogRepository
                .findByUser_UserIdInOrderByLoggedAtDesc(
                        memberIds
                );
    }
}
