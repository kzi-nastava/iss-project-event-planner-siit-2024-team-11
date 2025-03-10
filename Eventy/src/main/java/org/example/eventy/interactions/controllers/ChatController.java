package org.example.eventy.interactions.controllers;

import org.example.eventy.interactions.dtos.ChatDTO;
import org.example.eventy.interactions.dtos.MessageDTO;
import org.example.eventy.interactions.dtos.MessageListDTO;
import org.example.eventy.interactions.model.Chat;
import org.example.eventy.interactions.model.Message;
import org.example.eventy.interactions.services.ChatService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private ChatWebSocketController chatWebSocketController;

    @PostMapping(value = "/{otherId}")
    public ResponseEntity<?> createChat(@PathVariable Long otherId, @RequestHeader(value = "Authorization", required = false) String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Chat chat = chatService.getByEndUserOneAndTwo(user.getId(), otherId);
        if (chat == null) {
            User otherUser = userService.get(otherId);
            if (otherUser == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            chatService.createChat(user, otherUser);
        } else {
            chat.setLastMessageTime(LocalDateTime.now());
            chatService.saveChat(chat);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatDTO>> getUserChats(@RequestHeader(value = "Authorization", required = false) String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final User loggedInUser = user;
        List<Chat> allChats = chatService.getAllByEndUser(loggedInUser.getId());
        List<ChatDTO> chatDTOs = allChats.stream().map(v -> new ChatDTO(v, loggedInUser)).collect(Collectors.toList());

        return new ResponseEntity<List<ChatDTO>>(chatDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/messages/{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageListDTO> getMessages(@PathVariable long chatId, @RequestHeader(value = "Authorization", required = false) String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final User loggedInUser = user;
        Chat chat = chatService.getById(chatId);

        if (chat == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Message> messages = chatService.getMessagesByChat(chat);
        MessageListDTO messageListDTO = new MessageListDTO(messages, loggedInUser);

        return new ResponseEntity<>(messageListDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/messages/{chatId}")
    public ResponseEntity<?> sendMessage(@PathVariable long chatId, @RequestBody MessageDTO messageDTO, @RequestHeader(value = "Authorization", required = false) String token) {
        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Chat chat = chatService.getById(chatId);

        if (chat == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (chat.getEndUserOne().getBlocked().contains(user) || chat.getEndUserTwo().getBlocked().contains(user) ||
            user.getBlocked().contains(chat.getEndUserOne()) || user.getBlocked().contains(chat.getEndUserTwo())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Message createdMessage = chatService.addMessageToChat(chat, messageDTO);

        chatWebSocketController.sendMessage(chat.getEndUserTwo().getId(), new MessageDTO(createdMessage));
        chatWebSocketController.sendMessage(chat.getEndUserOne().getId(), new MessageDTO(createdMessage));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
