package org.example.eventy.users.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.models.Status;
import org.example.eventy.common.services.EmailService;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.common.util.ActiveUserManager;
import org.example.eventy.common.util.EncryptionUtil;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.Invitation;
import org.example.eventy.events.services.InvitationService;
import org.example.eventy.users.dtos.*;
import org.example.eventy.users.models.*;
import org.example.eventy.users.repositories.RoleRepository;
import org.example.eventy.users.services.RegistrationRequestService;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.NetworkUtils;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private PictureService pictureService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private ActiveUserManager activeUserManager;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDTO authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        // if user is reported / suspended for 3 days
        User user = userService.getUserByEmail(authenticationRequest.getEmail());
        if (user.getSuspensionDeadline()!= null && user.getSuspensionDeadline().plusDays(3).isAfter(LocalDateTime.now())) {
            LocalDateTime suspensionEnd = user.getSuspensionDeadline().plusDays(3);
            Duration remaining = Duration.between(LocalDateTime.now(), suspensionEnd);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String formattedSuspensionEnd = suspensionEnd.format(formatter);

            Map<String, Object> res = new HashMap<>();
            res.put("message", "Your account has been suspended.");
            res.put("suspensionEndsAt", formattedSuspensionEnd + "h");
            res.put("timeLeft", String.format("%d hours, %d minutes",
                    remaining.toHours(), remaining.toMinutesPart()));

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        user = (User) authentication.getPrincipal();

        if(!user.isActive() || user.isDeactivated()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // add the currently logged-in user, so we can track them (for accepted events - Tamara)
        activeUserManager.addUser(user);

        // go through all event invitations and add them to user's accepted events - Tamara
        acceptPendingInvitations(user);

        // Vrati token kao odgovor na uspesnu autentifikaciju
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getId()));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = tokenUtils.getUsernameFromToken(token);

        if (email != null && activeUserManager.isUserActive(email)) {
            activeUserManager.removeUser(email);
            return ResponseEntity.ok("User removed successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or inactive user.");
        }
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
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
            newOrganizer.setRole(roleRepository.findByName("ROLE_Organizer"));
            newOrganizer.setImageUrls(this.pictureService.save(registrationDTO.getProfilePictures()));

            newOrganizer = (EventOrganizer) userService.save(newOrganizer, true);
            if(newOrganizer == null) {
                return new ResponseEntity<String>("Registering organizer failed!", HttpStatus.INTERNAL_SERVER_ERROR);
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
            newProvider.setRole(roleRepository.findByName("ROLE_Provider"));
            newProvider.setImageUrls(this.pictureService.save(registrationDTO.getProfilePictures()));

            newProvider = (SolutionProvider) userService.save(newProvider, true);
            if(newProvider == null) {
                return new ResponseEntity<String>("Register provider failed!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            user = newProvider;
        }

        RegistrationRequest registrationRequest = registrationRequestService.create(user);
        if(registrationRequest == null) {
            return new ResponseEntity<String>("Creating request failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String url = "http://" + NetworkUtils.getLocalIpAddress() +":8080/api/authentication/confirm-registration-routing/";
            emailService.sendEmail(
                    user.getEmail(),
                    "Confirm registration",
                    "Click on this link to confirm registration (the link is valid in the next 24h): " +
                            "<a href=\"" + url + registrationRequest.getId() + "\">Activate account</a>"
            );
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Sending email failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Confirmation email sent to the email address!", HttpStatus.CREATED);
    }

    @PutMapping(value="/registration-confirmation/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTokenState> confirmRegistration(@PathVariable Long requestId, @RequestHeader("User-Agent") String userAgent) {
        RequestUpdateStatus status = registrationRequestService.update(requestId);

        if(status == RequestUpdateStatus.NOT_FOUND) {
            return new ResponseEntity<UserTokenState>(HttpStatus.NOT_FOUND);
        }
        else if(status == RequestUpdateStatus.TOO_LATE) {
            User user = registrationRequestService.getUserForRequest(requestId);

            RegistrationRequest registrationRequest = registrationRequestService.create(user);
            if(registrationRequest == null) {
                return new ResponseEntity<UserTokenState>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            try {
                String url = "http://" + NetworkUtils.getLocalIpAddress() +":8080/api/authentication/confirm-registration-routing/";
                emailService.sendEmail(
                        user.getEmail(),
                        "Confirm registration",
                        "Click on this link to confirm registration (the link is valid in the next 24h): " +
                                "<a href=\"" + url + registrationRequest.getId() + "\">Activate account</a>"
                );
            }
            catch (Exception e) {
                return new ResponseEntity<UserTokenState>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
        }

        User user = registrationRequestService.getUserForRequest(requestId);

        if(user == null) {
            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
        }

        // go through all event invitations and add them to new user's accepted events
        acceptPendingInvitations(user);

        user.setActive(true);
        user.setEnabled(true);
        userService.save(user, false);

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        return new ResponseEntity<UserTokenState>(new UserTokenState(jwt, expiresIn, user.getId()), HttpStatus.OK);
    }

    @GetMapping("/confirm-registration-routing/{requestId}")
    public ResponseEntity<Void> confirmRegistrationRouting(@PathVariable Long requestId, @RequestHeader("User-Agent") String userAgent) {
        // if the device is mobile
        if (userAgent.toLowerCase().contains("android")) {
            String deepLink = "eventy://confirm-registration?id=" + requestId;
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(deepLink));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        String webLink = "http://localhost:4200/confirm-registration/" + requestId;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(webLink));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/fast-registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> fastRegister(@Valid @RequestBody FastRegistrationDTO fastRegistrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        try {
            String email = EncryptionUtil.decrypt(fastRegistrationDTO.getEncryptedEmail());

            AuthenticatedUser newAuthenticatedUser = new AuthenticatedUser();
            newAuthenticatedUser.setEmail(email);
            newAuthenticatedUser.setPassword(fastRegistrationDTO.getPassword());
            newAuthenticatedUser.setAddress(fastRegistrationDTO.getAddress());
            newAuthenticatedUser.setPhoneNumber(fastRegistrationDTO.getPhoneNumber());
            newAuthenticatedUser.setActive(false);
            newAuthenticatedUser.setDeactivated(false);
            newAuthenticatedUser.setEnabled(false);
            newAuthenticatedUser.setHasSilencedNotifications(false);
            newAuthenticatedUser.setRole(roleRepository.findByName("ROLE_AuthenticatedUser"));
            newAuthenticatedUser.setImageUrls(null);

            newAuthenticatedUser = (AuthenticatedUser) userService.save(newAuthenticatedUser, true);
            if(newAuthenticatedUser == null) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

            RegistrationRequest registrationRequest = registrationRequestService.create(newAuthenticatedUser);
            if(registrationRequest == null) {
                return new ResponseEntity<String>("Creating request failed!", HttpStatus.BAD_REQUEST);
            }

            try {
                String url = "http://" + NetworkUtils.getLocalIpAddress() +":8080/api/authentication/confirm-registration-routing/";
                emailService.sendEmail(
                    newAuthenticatedUser.getEmail(),
                    "Confirm registration",
                    "Click on this link to confirm registration (the link is valid in the next 24h): " +
                            "<a href=\"" + url + registrationRequest.getId() + "\">Activate account</a>"
                );
            } catch (Exception e) {
                return new ResponseEntity<String>("Sending email failed!", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Confirmation email sent to the email address!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    private void acceptPendingInvitations(User user) {
        List<Event> acceptedEvents = user.getAcceptedEvents();
        List<Invitation> invitations = invitationService.getPendingInvitationsByGuestEmail(user.getEmail());
        for (Invitation invitation : invitations) {
            invitation.setStatus(Status.ACCEPTED);
            invitationService.save(invitation);
            acceptedEvents.add(invitation.getEvent());
        }
        if (!invitations.isEmpty()) {
            user.setAcceptedEvents(acceptedEvents);
            userService.save(user, false);
        }
    }

    @GetMapping("/fast-registration-routing/{encryptedEmail}")
    public ResponseEntity<Void> fastRegistrationRouting(@PathVariable String encryptedEmail, @RequestHeader("User-Agent") String userAgent) {
        // if the device is mobile
        if (userAgent.toLowerCase().contains("android")) {
            String deepLink = "eventy://fast-registration?value=" + encryptedEmail;
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(deepLink));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        String webLink = "http://localhost:4200/fast-registration?value=" + encryptedEmail;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(webLink));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/fast-registration/{encryptedData}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> decryptEmail(@PathVariable String encryptedData) {
        try {
            String decryptedEmail = EncryptionUtil.decrypt(encryptedData);
            return ResponseEntity.ok(decryptedEmail);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Decryption failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping(value = "/upgrade-profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> upgradeProfile(@Valid @RequestBody UpgradeProfileDTO upgradeProfileDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        try {
            User currentUser = userService.getUserByEmail(upgradeProfileDTO.getEmail());
            User newUser;

            // Upgrate to EVENT ORGANIZER
            if (upgradeProfileDTO.getAccountType().equals("EVENT ORGANIZER")) {
                List<PicturePath> images = pictureService.save(upgradeProfileDTO.getProfilePictures());
                if (images == null) {
                    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                }
                currentUser.setImageUrls(images);
                currentUser.setActive(false);
                currentUser.setDeactivated(false);
                currentUser.setEnabled(false);
                currentUser.setRole(roleRepository.findByName("ROLE_Organizer"));

                try {
                    currentUser = userService.save(currentUser, false);
                    if (currentUser == null) {
                        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                    }

                    String firstName = upgradeProfileDTO.getFirstName();
                    String lastName = upgradeProfileDTO.getLastName();
                    String userType = "Organizer";

                    currentUser = userService.upgradeUserToOrganizer(currentUser.getId(), firstName, lastName, userType);
                    if (currentUser ==  null) {
                        currentUser = userService.getUserByEmail(upgradeProfileDTO.getEmail());
                        currentUser.setActive(true);  // return the previous stats of user
                        currentUser.setDeactivated(false);
                        currentUser.setEnabled(true);
                        currentUser.setRole(roleRepository.findByName("ROLE_AuthenticatedUser"));

                        userService.save(currentUser , false);
                        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                }

                newUser = (User) currentUser;

            // Upgrate to SOLUTIONS PROVIDER
            } else {
                List<PicturePath> images = pictureService.save(upgradeProfileDTO.getProfilePictures());
                if (images == null) {
                    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                }
                currentUser.setImageUrls(images);
                currentUser.setActive(false);
                currentUser.setDeactivated(false);
                currentUser.setEnabled(false);
                currentUser.setRole(roleRepository.findByName("ROLE_Provider"));

                try {
                    currentUser = userService.save(currentUser, false);
                    if (currentUser == null) {
                        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                    }

                    String name = upgradeProfileDTO.getCompanyName();
                    String description = upgradeProfileDTO.getDescription();
                    String userType = "Provider";

                    currentUser = userService.upgradeUserToProvider(currentUser.getId(), name, description, userType);
                    if (currentUser ==  null) {
                        currentUser = userService.getUserByEmail(upgradeProfileDTO.getEmail());
                        currentUser.setActive(true); // return the previous stats of user
                        currentUser.setDeactivated(false);
                        currentUser.setEnabled(true);
                        currentUser.setRole(roleRepository.findByName("ROLE_AuthenticatedUser"));

                        userService.save(currentUser , false);
                        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                }

                newUser = (User) currentUser;
            }

            RegistrationRequest registrationRequest = registrationRequestService.create(newUser);
            if(registrationRequest == null) {
                return new ResponseEntity<String>("Creating request failed!", HttpStatus.BAD_REQUEST);
            }

            try {
                String url = "http://" + NetworkUtils.getLocalIpAddress() +":8080/api/authentication/confirm-registration-routing/";
                emailService.sendEmail(
                    newUser.getEmail(),
                    "Confirm upgrade",
                    "Click on this link to confirm upgrade (the link is valid in the next 24h): " +
                            "<a href=\"" + url + registrationRequest.getId() + "\">Activate account</a>"
                );
            } catch (Exception e) {
                return new ResponseEntity<String>("Sending email failed!", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Confirmation email sent to the email address!", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}