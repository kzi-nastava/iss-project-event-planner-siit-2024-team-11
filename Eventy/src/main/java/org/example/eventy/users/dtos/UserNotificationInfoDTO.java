package org.example.eventy.users.dtos;

import org.example.eventy.users.models.User;

import java.time.LocalDateTime;

public class UserNotificationInfoDTO {
    private Long userId;
    private Boolean areNotificationsMuted;
    private LocalDateTime lastReadNotifications;
    private Boolean hasNewNotifications;

    public UserNotificationInfoDTO() {}

    public UserNotificationInfoDTO(Long userId, Boolean areNotificationsMuted, LocalDateTime lastReadNotifications, Boolean hasNewNotifications) {
        this.userId = userId;
        this.areNotificationsMuted = areNotificationsMuted;
        this.lastReadNotifications = lastReadNotifications;
        this.hasNewNotifications = hasNewNotifications;
    }

    public UserNotificationInfoDTO(User user, boolean hasNewNotifications) {
        this.userId = user.getId();
        this.areNotificationsMuted = user.isHasSilencedNotifications();
        this.lastReadNotifications = user.getLastReadNotifications();
        this.hasNewNotifications = hasNewNotifications;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getAreNotificationsMuted() {
        return areNotificationsMuted;
    }

    public void setAreNotificationsMuted(Boolean areNotificationsMuted) {
        this.areNotificationsMuted = areNotificationsMuted;
    }

    public LocalDateTime getLastReadNotifications() {
        return lastReadNotifications;
    }

    public void setLastReadNotifications(LocalDateTime lastReadNotifications) {
        this.lastReadNotifications = lastReadNotifications;
    }

    public Boolean getHasNewNotifications() {
        return hasNewNotifications;
    }

    public void setHasNewNotifications(Boolean hasNewNotifications) {
        this.hasNewNotifications = hasNewNotifications;
    }

    @Override
    public String toString() {
        return "UserNotificationInfoDTO{" +
                "userId=" + userId +
                ", areNotificationsMuted=" + areNotificationsMuted +
                ", lastReadNotifications=" + lastReadNotifications +
                ", hasNewNotifications=" + hasNewNotifications +
                '}';
    }
}
