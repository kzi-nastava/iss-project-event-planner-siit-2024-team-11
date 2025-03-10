package org.example.eventy.interactions.dtos;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.interactions.model.Chat;
import org.example.eventy.users.models.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ChatDTO {
    private Long chatId;
    private Long otherId;
    private String otherName;
    private String otherPicture;
    private LocalDateTime lastMessageTime;

    public ChatDTO() {}

    public ChatDTO(Chat chat, User loggedInUser) {
        this.chatId = chat.getId();
        this.lastMessageTime = chat.getLastMessageTime();
        if (loggedInUser.getId().longValue() == chat.getEndUserOne().getId().longValue()) {
            switch (chat.getEndUserTwo().getRole().getName()) {
                case "ROLE_Organizer":
                    this.otherName = ((EventOrganizer) chat.getEndUserTwo()).getFirstName() + " " + ((EventOrganizer) chat.getEndUserTwo()).getLastName();
                    break;
                case "ROLE_Provider":
                    this.otherName = ((SolutionProvider) chat.getEndUserTwo()).getName();
                    break;
                case "ROLE_Admin":
                    this.otherName = ((Admin) chat.getEndUserTwo()).getFirstName() + " " + ((Admin) chat.getEndUserTwo()).getLastName();
                    break;
                case "ROLE_AuthenticatedUser":
                    this.otherName = ((AuthenticatedUser) chat.getEndUserTwo()).getEmail();
            }
            this.otherPicture = chat.getEndUserTwo().getImageUrls().stream().map(PicturePath::getPath).map(PictureService::getImage).collect(Collectors.toList()).get(0);
            this.otherId = chat.getEndUserTwo().getId();
        } else {
            switch (chat.getEndUserOne().getRole().getName()) {
                case "ROLE_Organizer":
                    this.otherName = ((EventOrganizer) chat.getEndUserOne()).getFirstName() + " " + ((EventOrganizer) chat.getEndUserOne()).getLastName();
                    break;
                case "ROLE_Provider":
                    this.otherName = ((SolutionProvider) chat.getEndUserOne()).getName();
                    break;
                case "ROLE_Admin":
                    this.otherName = ((Admin) chat.getEndUserOne()).getFirstName() + " " + ((Admin) chat.getEndUserOne()).getLastName();
                    break;
                case "ROLE_AuthenticatedUser":
                    this.otherName = ((AuthenticatedUser) chat.getEndUserOne()).getEmail();
            }
            this.otherId = chat.getEndUserOne().getId();
            this.otherPicture = chat.getEndUserOne().getImageUrls().stream().map(PicturePath::getPath).map(PictureService::getImage).collect(Collectors.toList()).get(0);
        }
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getOtherId() {
        return otherId;
    }

    public void setOtherId(Long otherId) {
        this.otherId = otherId;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherPicture() {
        return otherPicture;
    }

    public void setOtherPicture(String otherPicture) {
        this.otherPicture = otherPicture;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
