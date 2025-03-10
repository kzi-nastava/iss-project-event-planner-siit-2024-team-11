package org.example.eventy.interactions.repositories;

import org.example.eventy.interactions.model.Chat;
import org.example.eventy.interactions.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findByChat(Chat chat);
}
