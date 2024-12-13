package org.example.eventy.users.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.users.dtos.*;
import org.example.eventy.users.models.RegistrationRequest;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.RegistrationRequestService;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationRequestService registrationRequestService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTokenState> login(@RequestBody LoginDTO authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getId()));
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
        RequestUpdateStatus status = registrationRequestService.update(requestId);

        if(status == RequestUpdateStatus.NOT_FOUND) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }
        else if(status == RequestUpdateStatus.TOO_LATE) {
            // maybe send new reg request?
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        User user = registrationRequestService.getUserForRequest(requestId);
        user.setActive(true);
        user = userService.save(user);

        if(user == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
        }

        user.setActive(true);
        userService.save(user);

        return new ResponseEntity<UserDTO>(new UserDTO(user, userService.getUserType(user)), HttpStatus.OK);
    }
}