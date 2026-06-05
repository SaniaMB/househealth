package com.project.househealth.repositories;

import com.project.househealth.entity.HealthLog;
import com.project.househealth.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<Notification> findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(
            Long userId
    );

    Optional<Notification> findByNotificationIdAndUser_UserId(
            Long notificationId,
            Long userId
    );

}
