package com.project.househealth.service;

import com.project.househealth.dto.response.CareRelationshipResponse;
import com.project.househealth.entity.CareRelationship;
import com.project.househealth.entity.User;
import com.project.househealth.exception.IllegalOperationException;
import com.project.househealth.repositories.CareRelationshipRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CareRelationshipServiceImpl
        implements CareRelationshipService {

    private final CareRelationshipRepository repository;
    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final NotificationService notificationService;

    public CareRelationshipServiceImpl(
            CareRelationshipRepository repository,
            CurrentUserService currentUserService,
            UserService userService, NotificationService notificationService
    ) {
        this.repository = repository;
        this.currentUserService = currentUserService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public void observeUser(Long trackedUserId) {

        User observer = currentUserService.getCurrentUser();
        User trackedUser = userService.findUserById(trackedUserId);

        if (observer.getUserId().equals(trackedUserId)) {
            throw new IllegalArgumentException(
                    "Cannot observe yourself"
            );
        }

        boolean alreadyExists =
                repository.existsByObserver_UserIdAndTrackedUser_UserId(
                        observer.getUserId(),
                        trackedUserId
                );

        if (alreadyExists) {
            throw new IllegalOperationException(
                    "Already observing this user"
            );
        }

        notificationService.createNotification(
                trackedUser,
                "Care Circle Update",
                observer.getName()
                        + " started caring for you"
        );

        repository.save(
                new CareRelationship(observer, trackedUser)
        );
    }

    @Override
    public void stopObserving(Long trackedUserId) {

        Long observerId =
                currentUserService.getCurrentUserId();

        CareRelationship relationship =
                repository
                        .findByObserver_UserIdAndTrackedUser_UserId(
                                observerId,
                                trackedUserId
                        )
                        .orElseThrow(() ->
                                new IllegalOperationException(
                                        "Relationship not found"
                                ));

        notificationService.createNotification(
                relationship.getTrackedUser(),
                "Care Circle Update",
                relationship.getObserver().getName()
                        + " stopped caring for you"
        );

        repository.delete(relationship);
    }

    @Override
    public List<CareRelationshipResponse>
    getUsersIObserve() {

        return repository
                .findByObserver_UserId(
                        currentUserService
                                .getCurrentUserId()
                )
                .stream()
                .map(relationship -> {

                    CareRelationshipResponse response =
                            new CareRelationshipResponse();

                    response.setUserId(
                            relationship
                                    .getTrackedUser()
                                    .getUserId()
                    );

                    response.setUserName(
                            relationship
                                    .getTrackedUser()
                                    .getName()
                    );

                    return response;

                })
                .toList();
    }

    @Override
    public List<CareRelationshipResponse>
    getMyObservers() {

        return repository
                .findByTrackedUser_UserId(
                        currentUserService
                                .getCurrentUserId()
                )
                .stream()
                .map(relationship -> {

                    CareRelationshipResponse response =
                            new CareRelationshipResponse();

                    response.setUserId(
                            relationship
                                    .getObserver()
                                    .getUserId()
                    );

                    response.setUserName(
                            relationship
                                    .getObserver()
                                    .getName()
                    );

                    return response;

                })
                .toList();
    }
}
