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
    @Autowired
    private PictureService pictureService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private ActiveUserManager activeUserManager;

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
            newOrganizer.setRole(roleRepository.findByName("ROLE_Organizer"));
            newOrganizer.setImageUrls(this.pictureService.save(registrationDTO.getProfilePictures()));

            if(registrationDTO.getFirstName() == null || registrationDTO.getLastName() == null) {
                return new ResponseEntity<String>("Organizer validation failed!", HttpStatus.BAD_REQUEST);
            }

            newOrganizer = (EventOrganizer) userService.save(newOrganizer, true);
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
            newProvider.setRole(roleRepository.findByName("ROLE_Provider"));
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

            newProvider = (SolutionProvider) userService.save(newProvider, true);
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

        if(user == null) {
            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
        }

        user.setActive(true);
        user.setEnabled(true);
        userService.save(user, false);

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        return new ResponseEntity<UserTokenState>(new UserTokenState(jwt, expiresIn, user.getId()), HttpStatus.OK);
    }

    @PostMapping(value = "/fast-registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<UserTokenState> fastRegister(@Valid @RequestBody FastRegistrationDTO fastRegistrationDTO) {
        try {
            String email = EncryptionUtil.decrypt(fastRegistrationDTO.getEncryptedEmail());

            AuthenticatedUser newAuthenticatedUser = new AuthenticatedUser();
            newAuthenticatedUser.setEmail(email);
            newAuthenticatedUser.setPassword(fastRegistrationDTO.getPassword());
            newAuthenticatedUser.setAddress(fastRegistrationDTO.getAddress());
            newAuthenticatedUser.setPhoneNumber(fastRegistrationDTO.getPhoneNumber());
            newAuthenticatedUser.setActive(true);
            newAuthenticatedUser.setDeactivated(false);
            newAuthenticatedUser.setEnabled(true);
            newAuthenticatedUser.setHasSilencedNotifications(false);
            newAuthenticatedUser.setRole(roleRepository.findByName("ROLE_AuthenticatedUser"));
            newAuthenticatedUser.setImageUrls(null);

            newAuthenticatedUser = (AuthenticatedUser) userService.save(newAuthenticatedUser, true);
            if(newAuthenticatedUser == null) {
                return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
            }

            // go through all event invitations and add them to new user's accepted events
            acceptPendingInvitations(newAuthenticatedUser);

            String jwt = tokenUtils.generateToken(newAuthenticatedUser);
            int expiresIn = tokenUtils.getExpiredIn();
            return new ResponseEntity<UserTokenState>(new UserTokenState(jwt, expiresIn, newAuthenticatedUser.getId()), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
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

    @PostMapping(value = "/upgrade-profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<UserTokenState> upgradeProfile(@Valid @RequestBody UpgradeProfileDTO upgradeProfileDTO) {
        try {
            User currentUser = userService.getUserByEmail(upgradeProfileDTO.getEmail());

            if (upgradeProfileDTO.getAccountType().equals("EVENT ORGANIZER")) {
                EventOrganizer newEventOrganizer = new EventOrganizer();
                newEventOrganizer.setId(currentUser.getId());
                List<PicturePath> images = pictureService.save(upgradeProfileDTO.getProfilePictures());
                if(images == null) {
                    return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
                }
                newEventOrganizer.setImageUrls(images);
                newEventOrganizer.setEmail(upgradeProfileDTO.getEmail());
                newEventOrganizer.setPassword(currentUser.getPassword());
                newEventOrganizer.setAddress(currentUser.getAddress());
                newEventOrganizer.setPhoneNumber(currentUser.getPhoneNumber());
                newEventOrganizer.setActive(true);
                newEventOrganizer.setDeactivated(false);
                newEventOrganizer.setEnabled(true);
                newEventOrganizer.setHasSilencedNotifications(currentUser.isHasSilencedNotifications());
                newEventOrganizer.setRole(roleRepository.findByName("ROLE_Organizer"));
                newEventOrganizer.setFirstName(upgradeProfileDTO.getFirstName());
                newEventOrganizer.setLastName(upgradeProfileDTO.getLastName());

                userService.deletePhysicallyById(currentUser.getId());
                if (userService.get(currentUser.getId()) != null) {
                    return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
                }

                newEventOrganizer = (EventOrganizer) userService.save(newEventOrganizer, true);
                if(newEventOrganizer == null) {
                    // restore the deleted user
                    userService.save(currentUser, true);
                    return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
                }

                String jwt = tokenUtils.generateToken(newEventOrganizer);
                int expiresIn = tokenUtils.getExpiredIn();
                return new ResponseEntity<UserTokenState>(new UserTokenState(jwt, expiresIn, newEventOrganizer.getId()), HttpStatus.CREATED);

            } else {
                SolutionProvider newSolutionProvider = new SolutionProvider();
                newSolutionProvider.setId(currentUser.getId());
                List<PicturePath> images = pictureService.save(upgradeProfileDTO.getProfilePictures());
                if(images == null) {
                    return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
                }
                newSolutionProvider.setImageUrls(images);
                newSolutionProvider.setEmail(upgradeProfileDTO.getEmail());
                newSolutionProvider.setPassword(currentUser.getPassword());
                newSolutionProvider.setAddress(currentUser.getAddress());
                newSolutionProvider.setPhoneNumber(currentUser.getPhoneNumber());
                newSolutionProvider.setActive(true);
                newSolutionProvider.setDeactivated(false);
                newSolutionProvider.setEnabled(true);
                newSolutionProvider.setHasSilencedNotifications(currentUser.isHasSilencedNotifications());
                newSolutionProvider.setRole(roleRepository.findByName("ROLE_Provider"));
                newSolutionProvider.setDescription(upgradeProfileDTO.getDescription());
                newSolutionProvider.setName(upgradeProfileDTO.getCompanyName());

                userService.deletePhysicallyById(currentUser.getId());
                if (userService.get(currentUser.getId()) != null) {
                    return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
                }

                newSolutionProvider = (SolutionProvider) userService.save(newSolutionProvider, true);
                if(newSolutionProvider == null) {
                    // restore the deleted user
                    userService.save(currentUser, true);
                    return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
                }

                String jwt = tokenUtils.generateToken(newSolutionProvider);
                int expiresIn = tokenUtils.getExpiredIn();
                return new ResponseEntity<UserTokenState>(new UserTokenState(jwt, expiresIn, newSolutionProvider.getId()), HttpStatus.CREATED);
            }

        } catch (Exception e) {
            return new ResponseEntity<UserTokenState>(HttpStatus.BAD_REQUEST);
        }
    }
}