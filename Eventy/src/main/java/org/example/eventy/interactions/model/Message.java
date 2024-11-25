package org.example.eventy.interactions.model;

import org.example.eventy.users.models.User;

public class Message {
    private User sender;
    private User receiver;
    private String message;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Message() {

    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
