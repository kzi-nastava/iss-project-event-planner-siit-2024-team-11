package org.example.eventy.users.controllers;

import org.example.eventy.users.dtos.UpdateUserProfileDTO;
import org.example.eventy.users.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    // type ResponseEntity<UserDTO> ??? here and in Auth??? UserDTO shouldnt have pass and confPass???
    public ResponseEntity<String> updateProfile(@RequestBody UpdateUserProfileDTO updateUserProfileDTO) {
        if(updateUserProfileDTO.getEmail().equals("good@gmail.com")) {
            return new ResponseEntity<String>(HttpStatus.OK);
        }

        if(updateUserProfileDTO.getId() == 100) {
            return new ResponseEntity<String>("Validation failed!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/{userId}")
    public ResponseEntity<String> deactivateProfile(@PathVariable Long userId) {
        if(userId == 5) {
            return new ResponseEntity<String>(HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value="/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upgradeProfile(@RequestBody UserDTO userDTO) {
        if(userDTO.getEmail().equals("good@gmail.com")) {
            return new ResponseEntity<String>(HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    // 4. View my own profile (basic information, my calendar, my favorite events,
    // my favorite products/services, my organized events or products/services)
    // 5. View someone else's profile with their basic information and their organizes events/products/services
    @GetMapping(value="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long userId) {
        if(userId == 5) {
            // if my JWT says that this is the logged-in users profile, then we send his profile view
            // if it says it is someone else's profile, then we send the corresponding view
            return new ResponseEntity<UserDTO>(new UserDTO(), HttpStatus.OK);
        }

        return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
    }
}
