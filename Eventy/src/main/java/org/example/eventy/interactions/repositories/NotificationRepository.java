package org.example.eventy.interactions.repositories;

import org.example.eventy.interactions.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_notifications (notification_id, user_id) VALUES (:notificationId, :userId)", nativeQuery = true)
    void saveNotificationToStudent(@Param("notificationId") Long notificationId,
                                   @Param("userId") Long userId);
}
