package org.example.eventy.interactions.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.example.eventy.users.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_one_id", referencedColumnName = "id")
    @JsonManagedReference
    private User endUserOne;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "end_user_two_id", referencedColumnName = "id")
    @JsonManagedReference
    private User endUserTwo;

    @Column(nullable = false)
    private LocalDateTime lastMessageTime;

    public Chat() {}

    public Chat(User endUserOne, User endUserTwo, LocalDateTime lastMessageTime) {
        this.endUserOne = endUserOne;
        this.endUserTwo = endUserTwo;
        this.lastMessageTime = lastMessageTime;
    }

    public Chat(Long id, User endUserOne, User endUserTwo, LocalDateTime lastMessageTime) {
        this.id = id;
        this.endUserOne = endUserOne;
        this.endUserTwo = endUserTwo;
        this.lastMessageTime = lastMessageTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEndUserOne() {
        return endUserOne;
    }

    public void setEndUserOne(User endUserOne) {
        this.endUserOne = endUserOne;
    }

    public User getEndUserTwo() {
        return endUserTwo;
    }

    public void setEndUserTwo(User endUserTwo) {
        this.endUserTwo = endUserTwo;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
