package org.example.eventy.solutions.services;

import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.model.NotificationType;
import org.example.eventy.interactions.services.NotificationService;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.eventy.solutions.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    public ArrayList<Reservation> getReservationsByServiceId(Long serviceId) {
        Solution service = serviceService.getService(serviceId);
        return reservationRepository.findAllBySelectedService(service);
    }

    public ArrayList<Reservation> getReservationsByEventId(Long eventId) {
        Event event = eventService.getEvent(eventId);
        return reservationRepository.findAllBySelectedEvent(event);
    }

    public Page<Reservation> getReservationsByUserId(Long userId, Pageable pageable) {
        return reservationRepository.findAllByUserId(userId, pageable);
    }

    public Reservation createReservation(ReservationDTO reservation) {
        Reservation newReservation = new Reservation();
        newReservation.setSelectedEvent(eventService.getEvent(reservation.getSelectedEventId()));
        newReservation.setSelectedService(serviceService.getService(reservation.getSelectedServiceId()));
        newReservation.setReservationStartDateTime(reservation.getReservationStartDateTime());
        newReservation.setReservationEndDateTime(reservation.getReservationEndDateTime());

        return saveReservation(newReservation);
    }

    public List<Reservation> findOverlappingReservations(ReservationDTO newReservation) {
        LocalDateTime start = newReservation.getReservationStartDateTime();
        LocalDateTime end = newReservation.getReservationEndDateTime();
        Solution selectedService = serviceService.getService(newReservation.getSelectedServiceId());
        return reservationRepository.findByReservationStartDateTimeBeforeAndReservationEndDateTimeAfterAndSelectedService(
                end, start, selectedService);
    }

    public Reservation saveReservation(Reservation reservation) {
        try {
            return reservationRepository.save(reservation);
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> getReservationByProviderBetween(Long providerId, LocalDate startDateTime, LocalDate endDateTime) {
        return reservationRepository.findReservationsByProvider(providerId, startDateTime.atStartOfDay(), endDateTime.atStartOfDay());
    }

    @Scheduled(fixedRate = 60000) // 1 minute
    public void checkReservationsAndNotify() {
        List<Reservation> reservationsToNotify = reservationRepository.findReservationsToNotify();

        for (Reservation reservation : reservationsToNotify) {
            NotificationType type = NotificationType.REMINDER_SERVICE;
            Long redirectionId = reservation.getSelectedEvent().getId();
            String title = "Upcoming Service REMINDER";
            User owner = reservation.getSelectedEvent().getOrganiser();
            User grader = null;
            Integer grade = null;
            String message = "Your reserved service \"" + reservation.getSelectedService().getName() +
                             "\" for event \"" + reservation.getSelectedEvent().getName() + "\"" +
                             " will begin in 1 hour. Tap to see more!";
            LocalDateTime timestamp = LocalDateTime.now();
            Notification notification = new Notification(type, redirectionId, title, message, grader, grade, timestamp);

            notificationService.saveNotification(owner.getId(), notification);
            sendNotificationToWeb(owner.getId(), notification);
            sendNotificationToMobile(owner.getId(), notification);

            reservation.setNotificationSent(true);
            reservationRepository.save(reservation);
        }
    }

    private void sendNotificationToWeb(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/web/" + userId, notification);
    }

    private void sendNotificationToMobile(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/mobile/" + userId, notification);
    }
}
