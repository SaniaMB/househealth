package com.project.househealth.service;

import com.project.househealth.entity.Notification;
import com.project.househealth.entity.User;

import java.util.List;

public interface NotificationService {

    Notification createNotification(
            User user,
            String title,
            String message
    );

    List<Notification> getMyNotifications();

    List<Notification> getMyUnreadNotifications();

    void markAsRead(Long notificationId);
}
