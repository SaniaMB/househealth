package com.project.househealth.controllers;

import com.project.househealth.dto.response.NotificationResponse;
import com.project.househealth.entity.Notification;
import com.project.househealth.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
    getMyNotifications() {

        List<NotificationResponse> responses =
                notificationService
                        .getMyNotifications()
                        .stream()
                        .map(notification -> {

                            NotificationResponse response =
                                    new NotificationResponse();

                            response.setNotificationId(
                                    notification.getNotificationId()
                            );

                            response.setTitle(
                                    notification.getTitle()
                            );

                            response.setMessage(
                                    notification.getMessage()
                            );

                            response.setRead(
                                    notification.isRead()
                            );

                            response.setCreatedAt(
                                    notification.getCreatedAt()
                            );

                            return response;
                        })
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>>
    getMyUnreadNotifications() {

        List<NotificationResponse> responses =
                notificationService
                        .getMyUnreadNotifications()
                        .stream()
                        .map(notification -> {

                            NotificationResponse response =
                                    new NotificationResponse();

                            response.setNotificationId(
                                    notification.getNotificationId()
                            );

                            response.setTitle(
                                    notification.getTitle()
                            );

                            response.setMessage(
                                    notification.getMessage()
                            );

                            response.setRead(
                                    notification.isRead()
                            );

                            response.setCreatedAt(
                                    notification.getCreatedAt()
                            );

                            return response;
                        })
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId
    ) {

        notificationService.markAsRead(
                notificationId
        );

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }
}
