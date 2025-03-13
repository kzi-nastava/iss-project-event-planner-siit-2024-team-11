package org.example.eventy.interactions.dtos;

import org.example.eventy.interactions.model.Chat;
import org.example.eventy.interactions.model.Message;
import org.example.eventy.users.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class MessageListDTO {
    private List<MessageDTO> allMessages;

    public MessageListDTO() {}

    public MessageListDTO(List<Message> messages, User loggedInUser) {
        this.allMessages = messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    public List<MessageDTO> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(List<MessageDTO> allMessages) {
        this.allMessages = allMessages;
    }
}
