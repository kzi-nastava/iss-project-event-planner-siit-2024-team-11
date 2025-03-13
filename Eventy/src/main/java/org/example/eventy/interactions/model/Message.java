package org.example.eventy.interactions.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.example.eventy.users.models.User;

import java.time.LocalDateTime;

@Entity
@Table (name = "Messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @JsonManagedReference
    private User sender;

    @Column
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Message() {}

    public Message(Long id, Chat chat, User sender, String message, LocalDateTime timestamp) {
        this.id = id;
        this.chat = chat;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(Chat chat, User sender, String message, LocalDateTime timestamp) {
        this.chat = chat;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
