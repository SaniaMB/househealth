package com.project.househealth.service;

import com.project.househealth.entity.Notification;
import com.project.househealth.entity.User;
import com.project.househealth.exception.NotificationNotFoundException;
import com.project.househealth.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final CurrentUserService currentUserService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, CurrentUserService currentUserService) {
        this.notificationRepository = notificationRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Notification createNotification(User user, String title, String message) {

        Notification notification = new Notification(title, message, user);

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getMyNotifications() {
        User currentUser = currentUserService.getCurrentUser();

        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(currentUser.getUserId());
    }

    @Override
    public List<Notification> getMyUnreadNotifications() {
        User currentUser = currentUserService.getCurrentUser();

        return notificationRepository.findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(currentUser.getUserId());
    }

    @Override
    public void markAsRead(Long notificationId) {
        User currentUser = currentUserService.getCurrentUser();

        Notification notification =
                notificationRepository
                        .findByNotificationIdAndUser_UserId(
                                notificationId,
                                currentUser.getUserId()
                        )
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found"
                                )
                        );

        notification.markAsRead();

        notificationRepository.save(notification);
    }
}
