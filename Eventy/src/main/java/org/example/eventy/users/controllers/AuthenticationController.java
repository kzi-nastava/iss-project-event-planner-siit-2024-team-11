package org.example.eventy.users.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.users.dtos.*;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.RegistrationRequest;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.models.User;
import org.example.eventy.users.repositories.RoleRepository;
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
import java.util.List;

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

    @Autowired
    private RoleRepository roleRepository;

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

        if(!user.isActive() || user.isDeactivated()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getId()));
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
        User user;
        if(registrationDTO.getName() == null) {
            EventOrganizer newOrganizer = new EventOrganizer();
            newOrganizer.setFirstName(registrationDTO.getFirstName());
            newOrganizer.setLastName(registrationDTO.getLastName());
            newOrganizer.setPassword(registrationDTO.getPassword());
            newOrganizer.setEmail(registrationDTO.getEmail());
            newOrganizer.setAddress(registrationDTO.getAddress());
            newOrganizer.setPhoneNumber(registrationDTO.getPhoneNumber());
            newOrganizer.setActive(false);
            newOrganizer.setDeactivated(false);
            newOrganizer.setHasSilencedNotifications(false);
            newOrganizer.setRole(roleRepository.findByName("Organizer"));
            List<PicturePath> profilePictures = new ArrayList<>();
            for(String path : registrationDTO.getProfilePictures()) {
                PicturePath picturePath = new PicturePath();
                picturePath.setPath(path);
                profilePictures.add(picturePath);
                // no need for PictureService right?
            }
            newOrganizer.setImageUrls(profilePictures);

            if(registrationDTO.getFirstName() == null || registrationDTO.getLastName() == null) {
                return new ResponseEntity<String>("Organizer validation failed!", HttpStatus.BAD_REQUEST);
            }

            newOrganizer = (EventOrganizer) userService.save(newOrganizer);
            if(newOrganizer == null) {
                return new ResponseEntity<String>("Organizer validation failed!", HttpStatus.BAD_REQUEST);
            }

            user = newOrganizer;
        }
        else {
            SolutionProvider newProvider = new SolutionProvider();
            newProvider.setName(registrationDTO.getName());
            newProvider.setDescription(registrationDTO.getDescription());
            newProvider.setPassword(registrationDTO.getPassword());
            newProvider.setEmail(registrationDTO.getEmail());
            newProvider.setAddress(registrationDTO.getAddress());
            newProvider.setPhoneNumber(registrationDTO.getPhoneNumber());
            newProvider.setActive(false);
            newProvider.setDeactivated(false);
            newProvider.setHasSilencedNotifications(false);
            newProvider.setRole(roleRepository.findByName("Provider"));
            List<PicturePath> profilePictures = new ArrayList<>();
            for(String path : registrationDTO.getProfilePictures()) {
                PicturePath picturePath = new PicturePath();
                picturePath.setPath(path);
                profilePictures.add(picturePath);
                // no need for PictureService right?
            }
            newProvider.setImageUrls(profilePictures);

            if(registrationDTO.getName() == null || registrationDTO.getDescription() == null) {
                return new ResponseEntity<String>("Provider validation failed!", HttpStatus.BAD_REQUEST);
            }

            newProvider = (SolutionProvider) userService.save(newProvider);
            if(newProvider == null) {
                return new ResponseEntity<String>("Provider validation failed!", HttpStatus.BAD_REQUEST);
            }

            user = newProvider;
        }

        RegistrationRequest registrationRequest = registrationRequestService.create(user);
        if(registrationRequest == null) {
            return new ResponseEntity<String>("Creating request failed!", HttpStatus.BAD_REQUEST);
        }

        try {
            emailService.sendEmail(
                    user.getEmail(),
                    "Confirm registration",
                    "Click on this link to confirm registration (the link is valid in the next 24h): " +
                            "<a href=\"http://localhost:4200/confirm-registration/" + registrationRequest.getId() + "\">Activate account</a>"
            );
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Sending email failed!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Confirmation email sent to the email address!", HttpStatus.CREATED);
    }

    @PutMapping(value="/registration-confirmation/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTokenState> confirmRegistration(@PathVariable Long requestId) {
        RequestUpdateStatus status = registrationRequestService.update(requestId);

        if(status == RequestUpdateStatus.NOT_FOUND) {
            return new ResponseEntity<UserTokenState>(HttpStatus.NOT_FOUND);
        }
        else if(status == RequestUpdateStatus.TOO_LATE) {
            User user = registrationRequestService.getUserForRequest(requestId);

            RegistrationRequest registrationRequest = registrationRequestService.create(user);
            if(registrationRequest == null) {
                return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
            }

            try {
                emailService.sendEmail(
                        user.getEmail(),
                        "Confirm registration",
                        "Click on this link to confirm registration (the link is valid in the next 24h): " +
                                "<a href=\"http://localhost:4200/confirm-registration/" + registrationRequest.getId() + "\">Activate account</a>"
                );            }
            catch (Exception e) {
                return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
        }

        User user = registrationRequestService.getUserForRequest(requestId);
        user.setActive(true);
        user = userService.save(user);

        if(user == null) {
            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
        }

        user.setActive(true);
        user.setEnabled(true);
        userService.save(user);

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        return new ResponseEntity<UserTokenState>(new UserTokenState(jwt, expiresIn, user.getId()), HttpStatus.OK);
    }
}