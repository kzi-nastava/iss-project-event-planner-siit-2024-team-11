package org.example.eventy.users.controllers;

import jakarta.servlet.http.HttpServletResponse;
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