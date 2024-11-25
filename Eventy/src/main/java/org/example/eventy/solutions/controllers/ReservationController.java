package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /*
    {
        "id": 5,
        "selectedEventId": 3,
        "selectedServiceId": 12,
        "reservationStartDateTime": "2024-12-01T14:00:00",
        "reservationEndDateTime": "2024-12-01T16:00:00"
    }
    */
    // POST "/api/reservations"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservation) {
        if (reservation.getId() == 5) {
            ReservationDTO newReservation = reservationService.createReservation(reservation);
            return new ResponseEntity<>(newReservation, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // GET "/api/reservations/5"
    @GetMapping(value = "/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long reservationId) {
        if (reservationId == 5) {
            ReservationDTO reservation = reservationService.getReservation(reservationId);
            return new ResponseEntity<ReservationDTO>(reservation, HttpStatus.OK);
        }

        return new ResponseEntity<ReservationDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reservations/service/5"
    @GetMapping(value = "/service/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservationsByServiceId(@PathVariable Long serviceId) {
        ArrayList<ReservationDTO> reservations = reservationService.getReservationsByServiceId(serviceId);

        if (serviceId == 5) {
            return new ResponseEntity<Collection<ReservationDTO>>(reservations, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<ReservationDTO>>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reservations/event/5"
    @GetMapping(value = "/event/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservationsByEventId(@PathVariable Long eventId) {
        ArrayList<ReservationDTO> reservations = reservationService.getReservationsByEventId(eventId);

        if (eventId == 5) {
            return new ResponseEntity<Collection<ReservationDTO>>(reservations, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<ReservationDTO>>(HttpStatus.NOT_FOUND);
    }
}
