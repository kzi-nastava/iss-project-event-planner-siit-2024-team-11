package org.example.eventy.users.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.interactions.services.NotificationService;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.users.dtos.*;
import org.example.eventy.users.models.*;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("isAuthenticated()")
public class UserProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TokenUtils tokenUtils;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateProfile(@Valid @RequestBody UpdateUserProfileDTO updateUserProfileDTO) {
        User user = userService.get(updateUserProfileDTO.getId());

        if(user == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }

        if(!passwordEncoder.matches(updateUserProfileDTO.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        if(!user.getEmail().equals(updateUserProfileDTO.getEmail())) {
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        user.setAddress(updateUserProfileDTO.getAddress());
        user.setPhoneNumber(updateUserProfileDTO.getPhoneNumber());
        if(!updateUserProfileDTO.getPassword().isEmpty()) user.setPassword(updateUserProfileDTO.getPassword());
        user.setImageUrls(pictureService.save(updateUserProfileDTO.getProfilePictures()));

        UserType userType = userService.getUserType(user);
        if(userType == UserType.PROVIDER) {
            if (updateUserProfileDTO.getName().isEmpty() || updateUserProfileDTO.getDescription().isEmpty()) {
                return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
            }

            ((SolutionProvider) user).setName(updateUserProfileDTO.getName());
            ((SolutionProvider) user).setDescription(updateUserProfileDTO.getDescription());
        } else if (userType == UserType.ORGANIZER) {
            if (updateUserProfileDTO.getFirstName().isEmpty() || updateUserProfileDTO.getLastName().isEmpty()) {
                return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
            }

            ((EventOrganizer) user).setFirstName(updateUserProfileDTO.getFirstName());
            ((EventOrganizer) user).setLastName(updateUserProfileDTO.getLastName());
        } else if (userType == UserType.ADMIN) {
            if (updateUserProfileDTO.getFirstName().isEmpty() || updateUserProfileDTO.getLastName().isEmpty()) {
                return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
            }

            ((Admin) user).setFirstName(updateUserProfileDTO.getFirstName());
            ((Admin) user).setLastName(updateUserProfileDTO.getLastName());
        }

        user = userService.save(user, !updateUserProfileDTO.getPassword().isEmpty());

        if(user == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserDTO>(new UserDTO(user, userType), HttpStatus.OK);
    }

    @DeleteMapping(value="/{userId}")
    public ResponseEntity<?> deactivateProfile(@PathVariable Long userId) {
        User user = userService.get(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(!user.isEnabled() || user.isDeactivated() ||
            hasOrganizerFutureEvents(user) || hasProviderFutureReservedSolutions(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.setActive(false);
        user.setDeactivated(true);
        userService.save(user, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    private boolean hasOrganizerFutureEvents(User user) {
        if (!(user instanceof EventOrganizer)) {
            return false;
        }

        return !eventService.getOrganizedEventsByUserBetween(user.getId(), LocalDate.now(), LocalDate.of(9999, 12, 31)).isEmpty();
    }

    private boolean hasProviderFutureReservedSolutions(User user) {
        if (!(user instanceof SolutionProvider)) {
            return false;
        }

        return !reservationService.getReservationByProviderBetween(user.getId(), LocalDate.now(), LocalDate.of(9999, 12, 31)).isEmpty();
    }

    @PostMapping(value="/{userId}/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasRole('AuthenticatedUser')")
    public ResponseEntity<String> upgradeProfile(@RequestBody RegistrationDTO registrationDTO, @PathVariable Long userId) {
        if(registrationDTO.getEmail().equals("good@gmail.com")) {
            return new ResponseEntity<String>("Confirmation email sent!", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Validation failed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long userId,
                                              @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User loggedInUser;
        try {
            token = token.substring(7);
            loggedInUser = userService.findByEmail(tokenUtils.getUsernameFromToken(token));

            if(loggedInUser == null) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.get(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!user.getId().equals(loggedInUser.getId())) {
            if (loggedInUser.getBlocked().contains(user)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        if (user.isEnabled() && user.isActive() && !user.isDeactivated()) {
            return new ResponseEntity<UserDTO>(new UserDTO(user, userService.getUserType(user)), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{userId}/calendar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalendarOccupancyDTO>> getCalendar(@PathVariable Long userId,
                                                                  @RequestParam(required = false) LocalDate startDate,
                                                                  @RequestParam(required = false) LocalDate endDate) {
        List<CalendarOccupancyDTO> calendar = new ArrayList<>();

        for(Event event : eventService.getOrganizedEventsByUserBetween(userId, startDate, endDate)) {
            CalendarOccupancyDTO calendarOccupancyDTO = new CalendarOccupancyDTO(event.getName(), event.getId(),
                    OccupancyType.EVENT, event.getDate().toLocalDate(), event.getDate().toLocalDate());

            calendar.add(calendarOccupancyDTO);
        }

        for(Event event : eventService.getAttendingEventsByUserBetween(userId, startDate, endDate)) {
            CalendarOccupancyDTO calendarOccupancyDTO = new CalendarOccupancyDTO(event.getName(), event.getId(),
                    OccupancyType.EVENT, event.getDate().toLocalDate(), event.getDate().toLocalDate());

            calendar.add(calendarOccupancyDTO);
        }

        for(Reservation reservation : reservationService.getReservationByProviderBetween(userId, startDate, endDate)) {
            Solution service = reservation.getSelectedService();
            CalendarOccupancyDTO calendarOccupancyDTO = new CalendarOccupancyDTO(service.getName(), service.getId(),
                    OccupancyType.SERVICE, reservation.getReservationStartDateTime().toLocalDate(), reservation.getReservationEndDateTime().toLocalDate());

            calendar.add(calendarOccupancyDTO);
        }

        return new ResponseEntity<List<CalendarOccupancyDTO>>(calendar, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/notifications-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserNotificationInfoDTO> getUserNotificationsInfo(@PathVariable Long userId) {
        User user = userService.get(userId);

        if(user != null && user.isEnabled() && user.isActive() && !user.isDeactivated()) {
            boolean hasUserNewNotifications = notificationService.hasUserNewNotifications(userId);

            UserNotificationInfoDTO userNotificationInfoDTO = new UserNotificationInfoDTO(user, hasUserNewNotifications);
            return new ResponseEntity<UserNotificationInfoDTO>(userNotificationInfoDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{userId}/notifications-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> toggleNotifications(@PathVariable Long userId,
                                                       @RequestBody Boolean toggleValue) {
        User user = userService.get(userId);

        if(user != null) {
            user.setHasSilencedNotifications(toggleValue);

            user = userService.save(user, false);
            if (user == null) {
                return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<Boolean>(user.isHasSilencedNotifications(), HttpStatus.OK);
        }

        return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{userId}/last-read-notifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocalDateTime> updateLastReadNotifications(@PathVariable Long userId) {
        User user = userService.get(userId);

        if(user != null) {
            user.setLastReadNotifications(LocalDateTime.now());

            user = userService.save(user, false);
            if (user == null) {
                return new ResponseEntity<LocalDateTime>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<LocalDateTime>(user.getLastReadNotifications(), HttpStatus.OK);
        }

        return new ResponseEntity<LocalDateTime>(HttpStatus.NOT_FOUND);
    }

    // POST "/api/users/block"
    @PostMapping(value = "/block", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlockUserDTO> blockUser(@Valid @RequestBody BlockUserDTO blockUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        User blockedUser = userService.get(blockUserDTO.getBlockedId());
        if (blockedUser == null) {
            return new ResponseEntity<BlockUserDTO>(blockUserDTO, HttpStatus.BAD_REQUEST);
        }

        User blockerUser = userService.get(blockUserDTO.getBlockerId());
        if (blockerUser == null) {
            return new ResponseEntity<BlockUserDTO>(blockUserDTO, HttpStatus.BAD_REQUEST);
        }

        List<User> blockedUsers = blockerUser.getBlocked();
        blockedUsers.add(blockedUser);

        blockerUser = userService.save(blockerUser, false);
        if (blockerUser == null) {
            return new ResponseEntity<BlockUserDTO>(blockUserDTO, HttpStatus.BAD_REQUEST);
        }

        if ((blockerUser.getRole().getName().equals("ROLE_Organizer") && blockedUser.getRole().getName().equals("ROLE_AuthenticatedUser")) ||
            (blockedUser.getRole().getName().equals("ROLE_Organizer") && blockerUser.getRole().getName().equals("ROLE_AuthenticatedUser"))) {

            if (!blockedUser.getBlocked().contains(blockerUser)) {
                blockedUsers = blockedUser.getBlocked();
                blockedUsers.add(blockerUser);

                blockedUser = userService.save(blockedUser, false);
                if (blockedUser == null) {
                    return new ResponseEntity<BlockUserDTO>(blockUserDTO, HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<BlockUserDTO>(blockUserDTO, HttpStatus.OK);
    }
}
