package org.example.eventy.interactions.services;

import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.repositories.NotificationRepository;
import org.example.eventy.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;

    public Page<Notification> getNotificationsByUser(String userId, Pageable pageable) {
        return userRepository.findAllNotificationsByUserId(userId, pageable);
    }
}
