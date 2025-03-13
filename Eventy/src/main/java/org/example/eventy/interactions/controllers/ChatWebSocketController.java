package org.example.eventy.interactions.controllers;

import org.example.eventy.interactions.dtos.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @SendTo("/topic/chat/{userId}")
    public void sendMessage(@DestinationVariable Long userId, MessageDTO message) {
        messagingTemplate.convertAndSend("/topic/chat/" + userId, message);
    }

}
