package org.example.eventy.users.controllers;

import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.RegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("api/")
public class AuthenticationController {
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        if(Objects.equals(loginDTO.getEmail(), "good@email.com")) {
            return new ResponseEntity<String>("JWT Token", HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody RegisterDTO registerDTO) {
        if(Objects.equals(registerDTO.getEmail(), "good@email.com")) {
            return new ResponseEntity<String>(HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="register/{id}")
    public ResponseEntity<String> confirmRegistration(@PathVariable Long id) {
        if(id == 5) {
            return new ResponseEntity<String>("JWT Token", HttpStatus.OK);
            // redirect to the home page?
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        // redirect to some kind of error page?
    }
}
