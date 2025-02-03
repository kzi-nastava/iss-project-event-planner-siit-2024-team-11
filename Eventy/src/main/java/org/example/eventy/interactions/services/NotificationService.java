package org.example.eventy.interactions.services;

import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.repositories.NotificationRepository;
import org.example.eventy.users.models.User;
import org.example.eventy.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;

    public Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {
        return userRepository.findAllNotificationsByUserId(userId, pageable);
    }

    public boolean hasUserNewNotifications(Long userId) {
        return userRepository.hasNewNotifications(userId);
    }

    public Notification saveNotification(Long userId, Notification notification) {
        notification = notificationRepository.save(notification);
        notificationRepository.saveNotificationToStudent(notification.getId(), userId);

        return notification;
    }
}
