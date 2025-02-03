package org.example.eventy.interactions.controllers;

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
    public Notification sendNotificationToWeb(@DestinationVariable Long userId, Notification notification) {
        notification = notificationService.saveNotification(userId, notification);
        messagingTemplate.convertAndSend("/topic/web/" + userId, notification);
        return notification;
    }

    @MessageMapping("/sendNotificationToMobile/{userId}")
    @SendTo("/topic/mobile/{userId}")
    public Notification sendNotificationToMobile(@DestinationVariable Long userId, Notification notification) {
        notification = notificationService.saveNotification(userId, notification);
        messagingTemplate.convertAndSend("/topic/mobile/" + userId, notification);
        return notification;
    }
}
