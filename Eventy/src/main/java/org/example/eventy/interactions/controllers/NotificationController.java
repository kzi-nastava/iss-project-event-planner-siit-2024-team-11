package org.example.eventy.interactions.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.interactions.dtos.NotificationDTO;
import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<NotificationDTO>> getNotificationsByUserId(@PathVariable("userId") Long userId,
                                                                                   Pageable pageable) {
        Page<Notification> notifications = notificationService.getNotificationsByUserId(userId, pageable);

        List<NotificationDTO> notificationsDTO = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationsDTO.add(new NotificationDTO(notification));
        }
        long count = notifications.getTotalElements();

        PagedResponse<NotificationDTO> response = new PagedResponse<>(notificationsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<NotificationDTO>>(response, HttpStatus.OK);
    }
}
