package org.example.eventy.users.controllers;

import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ReservationService;
import org.example.eventy.users.dtos.*;
import org.example.eventy.users.models.*;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UpdateUserProfileDTO updateUserProfileDTO) {
        User user = userService.get(updateUserProfileDTO.getId());

        if(user == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }

        if(!passwordEncoder.matches(updateUserProfileDTO.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        user.setEmail(updateUserProfileDTO.getEmail());
        user.setAddress(updateUserProfileDTO.getAddress());
        user.setPhoneNumber(updateUserProfileDTO.getPhoneNumber());
        user.setPassword(updateUserProfileDTO.getPassword());
        user.setImageUrls(pictureService.save(updateUserProfileDTO.getProfilePictures())); // update!

        UserType userType = userService.getUserType(user);
        if(userType == UserType.PROVIDER) {
            ((SolutionProvider) user).setName(updateUserProfileDTO.getName());
            ((SolutionProvider) user).setDescription(updateUserProfileDTO.getDescription());
        } else if (userType == UserType.ORGANIZER) {
            ((EventOrganizer) user).setFirstName(updateUserProfileDTO.getFirstName());
            ((EventOrganizer) user).setLastName(updateUserProfileDTO.getLastName());
        } else if (userType == UserType.ADMIN) {
            ((Admin) user).setFirstName(updateUserProfileDTO.getFirstName());
            ((Admin) user).setLastName(updateUserProfileDTO.getLastName());
        } else {
            //((AuthenticatedUser) user).setFirstName(updateUserProfileDTO.getFirstName());
            //((AuthenticatedUser) user).setLastName(updateUserProfileDTO.getLastName());
            // <> <> <> NOTE: AuthenticatedUser does not have first nor last name - Tamara

        }

        user = userService.save(user, true);

        if(user == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<UserDTO>(new UserDTO(user, userType), HttpStatus.OK);
    }

    @DeleteMapping(value="/{userId}")
    public ResponseEntity<?> deactivateProfile(@PathVariable Long userId) {
        User user = userService.get(userId);
        if(user != null) {
            user.setActive(false);
            user.setDeactivated(true);
            userService.save(user, false);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long userId) {
        User user = userService.get(userId);

        if(user != null) {
            return new ResponseEntity<UserDTO>(new UserDTO(user, userService.getUserType(user)),
                    HttpStatus.OK);
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
}
