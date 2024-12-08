package org.example.eventy.users.controllers;

import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.services.EventService;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.dtos.*;
import org.example.eventy.users.models.*;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private PictureService pictureService;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UpdateUserProfileDTO updateUserProfileDTO) {
        User user = userService.get(updateUserProfileDTO.getId());

        if(user == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }

        if(!user.getPassword().equals(updateUserProfileDTO.getOldPassword())) {
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
            ((AuthenticatedUser) user).setFirstName(updateUserProfileDTO.getFirstName());
            ((AuthenticatedUser) user).setLastName(updateUserProfileDTO.getLastName());
        }

        user = userService.save(user);

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
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value="/{userId}/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
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
}
