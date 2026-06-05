package com.project.househealth.controllers;

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
   ResponseEntity<List<Notification>> getMyNotifications(){
       return ResponseEntity.ok(
               notificationService.getMyNotifications()
       );
   }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getMyUnreadNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyUnreadNotifications()
        );
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
