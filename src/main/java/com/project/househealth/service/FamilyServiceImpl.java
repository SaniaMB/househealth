package com.project.househealth.service;

import com.project.househealth.dto.response.FamilyMemberResponse;
import com.project.househealth.dto.response.FamilySummaryResponse;
import com.project.househealth.dto.response.MemberTrendSummaryResponse;
import com.project.househealth.entity.Family;
import com.project.househealth.entity.FamilyMembership;
import com.project.househealth.entity.User;
import com.project.househealth.enums.SystemRole;
import com.project.househealth.enums.TrendPeriod;
import com.project.househealth.exception.FamilyNotFoundException;
import com.project.househealth.exception.MembershipNotFoundException;
import com.project.househealth.exception.UnauthorizedFamilyActionException;
import com.project.househealth.repositories.FamilyMembershipRepository;
import com.project.househealth.repositories.FamilyRepository;
import com.project.househealth.trendanalysis.TrendAnalysisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;
    private final FamilyMembershipRepository familyMembershipRepository;
    private final CurrentUserService currentUserService;
    private final TrendAnalysisService trendAnalysisService;

    public FamilyServiceImpl(FamilyRepository familyRepository,
                             FamilyMembershipRepository familyMembershipRepository,
                             CurrentUserService currentUserService, TrendAnalysisService trendAnalysisService){
        this.familyRepository = familyRepository;
        this.familyMembershipRepository = familyMembershipRepository;
        this.currentUserService = currentUserService;
        this.trendAnalysisService = trendAnalysisService;
    }

    @Transactional
    @Override
    public Family createFamily(String familyName) {

        User creator = currentUserService.getCurrentUser();

        Family family = new Family(familyName);
        family.setCreatedBy(creator);
        family = familyRepository.save(family);

        FamilyMembership familyMembership = new FamilyMembership(creator, family);
        familyMembership.makeOwner();

        family.addMembership(familyMembership);

        return familyRepository.save(family);
    }

    private Family findFamilyById(Long id) {
        return familyRepository.findById(id)
                .orElseThrow(() ->
                        new FamilyNotFoundException("Family not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Family getFamilyById(Long id) {

        Long currentUserId = currentUserService.getCurrentUserId();

        familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(currentUserId, id)
                .orElseThrow(() ->
                        new FamilyNotFoundException("Family not found"));

        return familyRepository.findById(id)
                .orElseThrow(() ->
                        new FamilyNotFoundException("Family not found"));
    }

    private Family validateOwnership(Long familyId, Long currentUserId){

        Family family = getFamilyById(familyId);

        FamilyMembership membership =
                familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                        .orElseThrow(() -> new UnauthorizedFamilyActionException("You are not a member of this family"));

        if (!membership.isOwner()) {
            throw new UnauthorizedFamilyActionException("Only owner can perform this action");
        }

        return family;
    }

    @Transactional
    @Override
    public void renameFamily(Long familyId, String newName) {
        Family family = validateOwnership(familyId, currentUserService.getCurrentUserId());

        family.renameFamily(newName);
    }

    @Transactional
    @Override
    public void permanentlyDeleteFamily(Long familyId) {

        User user = currentUserService.getCurrentUser();

        if (user.getSystemRole() != SystemRole.ADMIN){
            throw  new UnauthorizedFamilyActionException("Only admin can permanently delete a family");
        }

        Family family = getFamilyById(familyId);

        familyRepository.delete(family);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FamilySummaryResponse> getMyFamilies() {

        Long currentUserId =
                currentUserService.getCurrentUserId();

        return familyMembershipRepository
                .findByUser_UserId(currentUserId)
                .stream()
                .map(membership -> {

                    FamilySummaryResponse response =
                            new FamilySummaryResponse();

                    response.setFamilyId(
                            membership.getFamily().getFamilyId()
                    );

                    response.setFamilyName(
                            membership.getFamily().getFamilyName()
                    );

                    response.setOwner(
                            membership.isOwner()
                    );

                    return response;

                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FamilyMemberResponse> getFamilyMembers(
            Long familyId
    ) {

        Long currentUserId = currentUserService.getCurrentUserId();

        familyMembershipRepository
                .findByUser_UserIdAndFamily_FamilyId(
                        currentUserId,
                        familyId
                )
                .orElseThrow(() ->
                        new FamilyNotFoundException("Family not found"));

        return familyMembershipRepository
                .findByFamily_FamilyId(familyId)
                .stream()
                .map(membership -> {

                    FamilyMemberResponse response =
                            new FamilyMemberResponse();

                    response.setUserId(
                            membership.getUser().getUserId()
                    );

                    response.setName(
                            membership.getUser().getName()
                    );

                    response.setOwner(
                            membership.isOwner()
                    );

                    return response;

                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberTrendSummaryResponse getMemberTrendSummary(Long familyId, Long userId) {

        Long currentUserId = currentUserService.getCurrentUserId();

        familyMembershipRepository.findByUser_UserIdAndFamily_FamilyId(currentUserId, familyId)
                                    .orElseThrow(() ->
                                            new UnauthorizedFamilyActionException("You are not a member of this family"));

        FamilyMembership targetMembership = familyMembershipRepository
                                            .findByUser_UserIdAndFamily_FamilyId(userId, familyId)
                                            .orElseThrow(() ->
                                                    new MembershipNotFoundException("User is not part of this family"));

        MemberTrendSummaryResponse response = new MemberTrendSummaryResponse();

        response.setUserId(userId);

        response.setUserName(targetMembership.getUser().getName());

        response.setBloodPressureTrend(trendAnalysisService
                        .analyzeBloodPressureTrend(
                                userId,
                                TrendPeriod.WEEK
                        )
                        .getTrendStatus()
                        .name()
        );

        response.setFastingSugarTrend(
                trendAnalysisService
                        .analyzeFastingSugarTrend(
                                userId,
                                TrendPeriod.WEEK
                        )
                        .getTrendStatus()
                        .name()
        );

        response.setPostMealSugarTrend(
                trendAnalysisService
                        .analyzePostMealSugarTrend(
                                userId,
                                TrendPeriod.WEEK
                        )
                        .getTrendStatus()
                        .name()
        );

        return response;
    }
}
