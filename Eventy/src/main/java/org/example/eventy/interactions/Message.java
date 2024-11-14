package org.example.eventy.interactions;

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
}
