package org.example.eventy.users.controllers;

import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.RegistrationDTO;
import org.example.eventy.users.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        if(loginDTO.getEmail().equals("good@gmail.com")) {
            return new ResponseEntity<>("JWT Token", HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
        if(registrationDTO.getEmail().equals("good@gmail.com")) {
            return new ResponseEntity<String>("Confirmation email sent to the email address!", HttpStatus.CREATED);
        }

        return new ResponseEntity<String>("Validation failed!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/registration-confirmation/{requestId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> confirmRegistration(@PathVariable Long requestId) {
        if(requestId == 5) {
            return new ResponseEntity<String>("JWT Token", HttpStatus.OK);
            // redirect to the home page?
        }

        if(requestId == 100) {
            return new ResponseEntity<String>("24 hours have passed!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Registration request not found!", HttpStatus.NOT_FOUND);
        // redirect to some kind of error page?
    }
}