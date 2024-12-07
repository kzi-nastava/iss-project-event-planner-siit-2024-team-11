package org.example.eventy.users.controllers;

import org.example.eventy.users.dtos.RegistrationDTO;
import org.example.eventy.users.dtos.UpdateUserProfileDTO;
import org.example.eventy.users.dtos.UserDTO;
import org.example.eventy.users.dtos.UserType;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {
    @Autowired
    private UserService userService;

    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UpdateUserProfileDTO updateUserProfileDTO, @PathVariable Long userId) {
        UserDTO userDTO = new UserDTO();
        if(userId.equals(updateUserProfileDTO.getId()) && updateUserProfileDTO.getEmail().equals("good@gmail.com")) {
            userDTO.setEmail(updateUserProfileDTO.getEmail());
            userDTO.setId(updateUserProfileDTO.getId());
            userDTO.setProfilePictures(updateUserProfileDTO.getProfilePictures());
            userDTO.setUserType(UserType.ORGANIZER);
            userDTO.setFirstName(updateUserProfileDTO.getFirstName());
            userDTO.setLastName(updateUserProfileDTO.getLastName());
            userDTO.setAddress(updateUserProfileDTO.getAddress());
            userDTO.setPhoneNumber(updateUserProfileDTO.getPhoneNumber());
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        }

        if(updateUserProfileDTO.getId() == 100) {
            // validation failed
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.BAD_REQUEST);
        }

        // user not found
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value="/{userId}")
    public ResponseEntity<?> deactivateProfile(@PathVariable Long userId) {
        if(userId == 5) {
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

    // 4. View my own profile (basic information, my calendar, my favorite events,
    // my favorite products/services, my organized events or products/services)
    // 5. View someone else's profile with their basic information and their organizes events/products/services
    @GetMapping(value="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long userId) {
        UserDTO userDTO = new UserDTO();
        if(userId == 5) {
            // if my JWT says that this is the logged-in users profile, then we send his profile view
            // if it says it is someone else's profile, then we send the corresponding view
            userDTO.setEmail("good@gmail.com");
            userDTO.setId(5L);
            userDTO.setProfilePictures(new ArrayList<>());
            userDTO.setUserType(UserType.ORGANIZER);
            userDTO.setFirstName("Ime");
            userDTO.setLastName("Prezime");
            userDTO.setAddress("Neka Adresa");
            userDTO.setPhoneNumber("+13482192329");
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        }

        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.NOT_FOUND);
    }
}
