package org.example.eventy.interactions.controllers;

import org.example.eventy.interactions.dtos.NotificationDTO;
import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/sendNotificationToWeb/{userId}")
    @SendTo("/topic/web/{userId}")
    public NotificationDTO sendNotificationToWeb(@DestinationVariable Long userId, Notification notification) {
        notification = notificationService.saveNotification(userId, notification);
        NotificationDTO notificationDTO = new NotificationDTO(notification);
        messagingTemplate.convertAndSend("/topic/web/" + userId, notificationDTO);
        return notificationDTO;
    }

    @MessageMapping("/sendNotificationToMobile/{userId}")
    @SendTo("/topic/mobile/{userId}")
    public NotificationDTO sendNotificationToMobile(@DestinationVariable Long userId, Notification notification) {
        notification = notificationService.saveNotification(userId, notification);
        NotificationDTO notificationDTO = new NotificationDTO(notification);
        messagingTemplate.convertAndSend("/topic/mobile/" + userId, notificationDTO);
        return notificationDTO;
    }
}
