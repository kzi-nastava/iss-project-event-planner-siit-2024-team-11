package org.example.eventy.users.controllers;

import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.RegistrationDTO;
import org.example.eventy.users.dtos.UserDTO;
import org.example.eventy.users.dtos.UserType;
import org.example.eventy.users.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDTO) {
        UserDTO userDTO = new UserDTO();

        if(loginDTO.getEmail().equals("good@gmail.com")) {
            userDTO.setEmail("good@gmail.com");
            userDTO.setId(5L);
            userDTO.setProfilePictures(new ArrayList<>());
            userDTO.setUserType(UserType.ORGANIZER);
            userDTO.setFirstName("Ime");
            userDTO.setLastName("Prezime");
            userDTO.setAddress("Neka Adresa");
            userDTO.setPhoneNumber("+13482192329");
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }

        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
        if(registrationDTO.getEmail().equals("good@gmail.com")) {
            return new ResponseEntity<String>("Confirmation email sent to the email address!", HttpStatus.CREATED);
        }

        return new ResponseEntity<String>("Validation failed!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/registration-confirmation/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> confirmRegistration(@PathVariable Long requestId) {
        UserDTO userDTO = new UserDTO();
        if(requestId == 5) {
            userDTO.setEmail("good@gmail.com");
            userDTO.setId(5L);
            userDTO.setProfilePictures(new ArrayList<>());
            userDTO.setUserType(UserType.ORGANIZER);
            userDTO.setFirstName("Ime");
            userDTO.setLastName("Prezime");
            userDTO.setAddress("Neka Adresa");
            userDTO.setPhoneNumber("+13482192329");
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
            // redirect to the home page?
        }

        if(requestId == 100) {
            // 24 hours have passed
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.BAD_REQUEST);
        }

        // registration request not found
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.NOT_FOUND);
        // redirect to some kind of error page?
    }
}