package org.example.eventy.interactions.dtos;

import org.example.eventy.interactions.model.Message;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long senderId;
    private String message;
    private LocalDateTime timestamp;

    public MessageDTO() {}

    public MessageDTO(Message message) {
        this.senderId = message.getSender().getId();
        this.message = message.getMessage();
        this.timestamp = message.getTimestamp();
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
