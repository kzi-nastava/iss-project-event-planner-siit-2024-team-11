package org.example.eventy.common.services;

import jakarta.activation.FileDataSource;
import jakarta.mail.internet.InternetAddress;
import org.example.eventy.common.models.Status;
import org.example.eventy.common.util.ActiveUserManager;
import org.example.eventy.common.util.EncryptionUtil;
import org.example.eventy.events.dtos.CreateLocationDTO;
import org.example.eventy.events.dtos.OrganizeEventDTO;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.Invitation;
import org.example.eventy.events.models.Location;
import org.example.eventy.events.services.InvitationService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;
    @Autowired
    InvitationService invitationService;
    @Autowired
    private ActiveUserManager activeUserManager;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // Set true for HTML content

        mailSender.send(message);
    }

    public void sendInvitations(Event event, List<String> emails) {
        String organizerEmail = event.getOrganiser().getEmail();
        String eventName = event.getName();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        String eventDate = event.getDate().format(dateTimeFormatter);
        Location eventLocation = event.getLocation();

        for (String email : emails) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(new InternetAddress("eventy.app.team11@gmail.com", "Eventy"));
                helper.setTo(email);
                helper.setSubject("\uD83D\uDCC5  You're invited to '" + eventName + "'!");

                // create HTML content
                String htmlContent;
                String homepageLink = "http://localhost:4200/";

                // encrypted email in link with expiration date (1 day)
                long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
                String encryptedEmail = EncryptionUtil.encrypt(email, expirationTime);
                String registrationLink = "http://localhost:4200/fast-registration?value=" + encryptedEmail;

                String organizerEmailLink = "mailto:" + organizerEmail + "?subject=Event%20Invitation&body=Hello%2C%0D%0A%0D%0AI%20would%20like%20to%20know%20more%20about%20the%20event%20'" + eventName + "'.%0D%0A%0D%0ABest%20regards%2C%0D%0A";
                String eventyLogoSrc = "src/main/resources/static/logo-nav.png";
                String eventDetailsLink = "http://localhost:4200/events/5";

                User user = userService.findByEmail(email);
                // depending on the user, send the right email
                String type = (user != null) ? "normal_invite" : "fast_registration";
                htmlContent = buildEmailContent(type, email, organizerEmail, eventName, eventDate, eventLocation, homepageLink, registrationLink, organizerEmailLink, eventyLogoSrc, eventDetailsLink);
                helper.setText(htmlContent, true);

                // add Eventy Logo image --> <img src="cid:logoImage" alt="Eventy Logo"/>
                File logoFile = new File(eventyLogoSrc);
                if (!logoFile.exists()) {
                    throw new IllegalArgumentException("Logo file not found at: " + eventyLogoSrc);
                }
                helper.addInline("logoImage", new FileDataSource(logoFile)); // `cid:logoImage` matches HTML

                mailSender.send(message);

                Invitation invitation = new Invitation();
                invitation.setGuestEmail(email);
                invitation.setEvent(event);

                // NOTE: need to add if the user is logged in currently --> immediately update his accepted events!!
                if (user != null && activeUserManager.isUserActive(email)) {
                    List<Event> acceptedEvents = user.getAcceptedEvents();
                    acceptedEvents.add(event);
                    user.setAcceptedEvents(acceptedEvents);
                    userService.save(user, false);
                    invitation.setStatus(Status.ACCEPTED);
                } else {
                    invitation.setStatus(Status.PENDING);
                }

                invitationService.save(invitation);

            } catch (MessagingException e) {
                e.printStackTrace();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String buildEmailContent(String type, String recipientEmail, String organizerEmail, String eventName, String eventDate,
                                     Location location, String homepageLink, String registrationLink, String organizerEmailLink, String eventyLogoSrc, String eventLink) {
        String googleMapsLink = "https://www.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();

        if (type.equals("normal_invite")) {
            return """
            <!DOCTYPE html>
            <html>
               <head>
                 <meta name="color-scheme" content="light">
                 <meta name="supported-color-schemes" content="light">
                 <style>
                    body {
                       font-family: Arial, sans-serif;
                       background-color: #ffffff !important;
                       margin: 0;
                       padding: 0;
                    }
                       .container {
                          max-width: 680px;
                          margin: 5px auto;
                          background: #929AB7 !important;
                          border-radius: 10px;
                          overflow: hidden;
                       }
                          .header {
                             background-color: #929AB7 !important;
                             color: white;         \s
                          }
                         \s
                             .header p {
                                font-size: 23px;
                                font-weight: 700;
                                padding: 23px;
                                padding-top: 17px;
                                padding-bottom: 17px;
                                margin: 0px;
                                line-height: 30px;
                                color: white;
                                letter-spacing: 1px; \s
                             }
           
                             .header img {
                                width: 145px;
                                height: 26px;             \s
                             }
           
                       .content {
                          padding: 15px;
                          margin-left: 16px;
                          margin-right: 16px;
                          color: #515151 !important;
                          background-color: white !important;
                          border-radius: 10px;
                       }
                          .content p {
                             margin: 0;
                             font-size: 15.5px;
                             line-height: 26px;
                          }
           
                          .button {
                             display: inline-block;
                             background-color: #8890ac !important;
                             color: white !important;
                             text-decoration: none;
                             padding: 7px 15px;
                             border-radius: 7px;
                             color: white !important;
                             margin: 7px 0px;
                             margin-bottom: 10px;
                          }
           
                          .line_container {
                             display: flex;
                             flex-direction: column;
                             justify-content: center;
                             align-items: center;
                             position: relative;
                          }
                             .line {
                                width: 100%;
                                height: 1px;
                                background: rgb(221, 221, 221) !important;
                             }
           
                       .footer {
                          text-align: center;
                          padding: 5px;
                          font-size: 14.5px;
                          color: #ffffff !important;
                          letter-spacing: 0.2px;
                       }
                 </style>
              </head>
              <body>
                 <div class="container">
                    <div class="header">\s
                       <div style = "margin-left: 20px; margin-top: 17.5px;">
                          <a href="${homepageLink}">
                             <div>
                                <img src="cid:logoImage" alt=""/>
                             </div>
                          </a> \s
                       </div>
                       <p>You're invited to an Event!</p>
                    </div>
                    <div class="content">
                       <p>Dear <strong>${recipientEmail}</strong>,</p>
                       <p>You are invited to "<strong>${eventName}</strong>", organized by\s
                       <strong>${organizerEmail}</strong>.</p>
                       <p style = "color: #929AB7; font-size: 19px; letter-spacing: 0.5px; font-weight: 600; margin-top: 20px">Event Details:</p>
                       <ul style = "padding-left: 25px; margin: 0; margin-bottom: 18px; margin-top: 8px; ">
                          <li style = "margin-bottom: 5px; font-size: 16px;"><strong>Date:</strong> ${eventDate}</li>
                          <li style = "font-size: 16px;"><strong>Location:</strong> ${location.getName()} (${location.getAddress()})</li>
                       </ul>
                       <p>📍 View the location on the map:</p>
                       <p style = "margin-bottom: 15px">
                          <a href="${googleMapsLink}" class="button" target="_blank">Open in Google Maps</a>
                       </p>
                       <p>You can see more details here: <a href="${eventLink}">See Event Details</a>.</p>
                       <p style = "margin-bottom: 20px; margin-top: 15px">We look forward to seeing you! 😊</p>
                      \s
                       <div class = "line_container">
                          <div class = "line"></div>
                       </div>
           
                       <div style = "color: rgb(171, 171, 171); margin-top: 9px; margin-bottom: 9px">          \s
                          <p style = "font-family: monospace; font-size: 14.5px; line-height: 17.5px; margin-bottom: 8px;">This is an automated message. Please do not reply.</p>
                          <p style = "font-family: monospace; font-size: 14.5px; line-height: 17.5px;">If you require any assistance or information regarding the event, please contact the event organizer: <a href="${organizerEmailLink}">${organizerEmail}</a>.</p>
                       </div>
                    </div>
           
                    <div class="footer">
                       <p>&copy; 2024 Eventy. All rights reserved.</p>
                    </div>
                 </div>
              </body>
            </html>
            """
            .replace("${recipientEmail}", recipientEmail)
            .replace("${organizerEmail}", organizerEmail)
            .replace("${eventName}", eventName)
            .replace("${eventDate}", eventDate)
            .replace("${location.getName()}", location.getName())
            .replace("${location.getAddress()}", location.getAddress())
            .replace("${googleMapsLink}", googleMapsLink)
            .replace("${homepageLink}", homepageLink)
            .replace("${eventLink}", eventLink)
            .replace("${organizerEmailLink}", organizerEmailLink)
            .replace("${eventyLogoSrc}", eventyLogoSrc);
        } else { // "fast_registration"
            return """
            <!DOCTYPE html>
            <html>
               <head>
                  <meta name="color-scheme" content="light">
                  <meta name="supported-color-schemes" content="light">
                  <style>
                     body {
                        font-family: Arial, sans-serif;
                        background-color: #ffffff !important;
                        margin: 0;
                        padding: 0;
                     }
                        .container {
                           max-width: 680px;
                           margin: 5px auto;
                           background: #929AB7 !important;
                           border-radius: 10px;
                           overflow: hidden;
                        }
                           .header {
                              background-color: #929AB7 !important;
                              color: white;         \s
                           }
                          \s
                              .header p {
                                 font-size: 23px;
                                 font-weight: 700;
                                 padding: 23px;
                                 padding-top: 17px;
                                 padding-bottom: 17px;
                                 margin: 0px;
                                 line-height: 30px;
                                 color: white;
                                 letter-spacing: 1px; \s
                              }
            
                              .header img {
                                 width: 145px;
                                 height: 26px;             \s
                              }
            
                        .content {
                           padding: 15px;
                           margin-left: 16px;
                           margin-right: 16px;
                           color: #515151 !important;
                           background-color: white !important;
                           border-radius: 10px;
                        }
                           .content p {
                              margin: 0;
                              font-size: 15.5px;
                              line-height: 26px;
                           }
            
                           .button {
                              display: inline-block;
                              background-color: #8890ac !important;
                              color: white !important;
                              text-decoration: none;
                              padding: 7px 15px;
                              border-radius: 7px;
                              color: white !important;
                              margin: 7px 0px;
                              margin-bottom: 10px;
                           }
            
                           .line_container {
                              display: flex;
                              flex-direction: column;
                              justify-content: center;
                              align-items: center;
                              position: relative;
                           }
                              .line {
                                 width: 100%;
                                 height: 1px;
                                 background: rgb(221, 221, 221) !important;
                              }
            
                        .footer {
                           text-align: center;
                           padding: 5px;
                           font-size: 14.5px;
                           color: #ffffff !important;
                           letter-spacing: 0.2px;
                        }
                  </style>
               </head>
               <body>
                  <div class="container">
                     <div class="header">\s
                        <div style = "margin-left: 20px; margin-top: 17.5px;">
                           <a href="${homepageLink}">
                              <div>
                                 <img src="cid:logoImage" alt=""/>
                              </div>
                           </a> \s
                        </div>
                        <p>You're invited to an Event!</p>
                     </div>
                     <div class="content">
                        <p>Dear <strong>${recipientEmail}</strong>,</p>
                        <p>You are invited to "<strong>${eventName}</strong>", organized by\s
                        <strong>${organizerEmail}</strong>.</p>
                        <p style = "color: #929AB7; font-size: 19px; letter-spacing: 0.5px; font-weight: 600; margin-top: 20px">Event Details:</p>
                        <ul style = "padding-left: 25px; margin: 0; margin-bottom: 18px; margin-top: 8px; ">
                           <li style = "margin-bottom: 5px; font-size: 16px;"><strong>Date:</strong> <span style = "color: rgb(155, 155, 155)">available after registration</span></li>
                           <li style = "font-size: 16px;"><strong>Location:</strong> <span style = "color: rgb(155, 155, 155)">available after registration</span></li>
                        </ul>    \s
                        <p>To accept the invitation to the event and see more details, <strong>you need to be registered</strong>.</p>
                        <p>You can quickly <a href="${registrationLink}">Register Here</a>. After the registration, the invitation will be <strong>accepted</strong> and the event will be added to your profile.</p>
                        <p style = "margin-bottom: 20px; margin-top: 15px">We look forward to seeing you! 😊</p>
                       \s
                        <div class = "line_container">
                           <div class = "line"></div>
                        </div>
            
                        <div style = "color: rgb(171, 171, 171); margin-top: 9px; margin-bottom: 9px">          \s
                           <p style = "font-family: monospace; font-size: 14.5px; line-height: 17.5px; margin-bottom: 8px;">This is an automated message. Please do not reply.</p>
                           <p style = "font-family: monospace; font-size: 14.5px; line-height: 17.5px;">If you require any assistance or information regarding the event, please contact the event organizer: <a href="${organizerEmailLink}">${organizerEmail}</a>.</p>
                        </div>
                     </div>
            
                     <div class="footer">
                        <p>&copy; 2024 Eventy. All rights reserved.</p>
                     </div>
                  </div>
               </body>
            </html>
            """
            .replace("${recipientEmail}", recipientEmail)
            .replace("${organizerEmail}", organizerEmail)
            .replace("${eventName}", eventName)
            .replace("${homepageLink}", homepageLink)
            .replace("${registrationLink}", registrationLink)
            .replace("${organizerEmailLink}", organizerEmailLink)
            .replace("${eventyLogoSrc}", eventyLogoSrc);
        }
    }
}