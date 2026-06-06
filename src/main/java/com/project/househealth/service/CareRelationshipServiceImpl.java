package com.project.househealth.service;

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

    public CareRelationshipServiceImpl(
            CareRelationshipRepository repository,
            CurrentUserService currentUserService,
            UserService userService
    ) {
        this.repository = repository;
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @Override
    public void observeUser(Long trackedUserId) {

        User observer = currentUserService.getCurrentUser();
        User trackedUser = userService.getUserById(trackedUserId);

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

        repository.delete(relationship);
    }

    @Override
    public List<CareRelationship> getUsersIObserve() {

        return repository.findByObserver_UserId(
                currentUserService.getCurrentUserId()
        );
    }

    @Override
    public List<CareRelationship> getMyObservers() {

        return repository.findByTrackedUser_UserId(
                currentUserService.getCurrentUserId()
        );
    }
}
