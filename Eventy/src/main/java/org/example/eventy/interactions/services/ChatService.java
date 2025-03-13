package org.example.eventy.interactions.services;

import org.example.eventy.interactions.dtos.MessageDTO;
import org.example.eventy.interactions.model.Chat;
import org.example.eventy.interactions.model.Message;
import org.example.eventy.interactions.repositories.ChatRepository;
import org.example.eventy.interactions.repositories.MessageRepository;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    public Chat saveChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat getById(long id) {
        return chatRepository.findById(id).orElse(null);
    }

    public List<Chat> getAllByEndUser(Long endUserId) {
        return chatRepository.findAllByEndUser(endUserId);
    }

    public Chat getByEndUserOneAndTwo(Long endUserOne, Long endUserTwo) {
        return chatRepository.findByEndUserOneAndEndUserTwo(endUserOne, endUserTwo);
    }

    public Message addMessageToChat(Chat chat, MessageDTO messageDTO) {
        chat.setLastMessageTime(messageDTO.getTimestamp());
        chatRepository.save(chat);
        return messageRepository.save(new Message(chat, userService.get(messageDTO.getSenderId()), messageDTO.getMessage(), messageDTO.getTimestamp()));
    }

    public List<Message> getMessagesByChat(Chat chat) {
        return messageRepository.findByChat(chat);
    }

    public Chat createChat(User endUserOne, User endUserTwo) {
        Chat chat = new Chat(endUserOne, endUserTwo, LocalDateTime.now());
        chatRepository.save(chat);
        return chat;
    }
}
