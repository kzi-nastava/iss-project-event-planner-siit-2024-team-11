package org.example.eventy.solutions.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.solutions.dtos.ReservationDTO;
import org.example.eventy.solutions.models.Reservation;
import org.example.eventy.solutions.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    // GET "/api/reservations/5"
    @GetMapping(value = "/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservation(reservationId);

        if (reservation != null) {
            ReservationDTO reservationDTO = new ReservationDTO(reservation);
            return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.OK);
        }

        return new ResponseEntity<ReservationDTO>(HttpStatus.NOT_FOUND);
    }

    // NOTE: IDK if this method is even needed... if it is, though, Pageable needs to be added!
    // GET "/api/reservations/service/5"
    @GetMapping(value = "/service/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservationsByServiceId(@PathVariable Long serviceId) {
        ArrayList<Reservation> reservations = reservationService.getReservationsByServiceId(serviceId);

        if (reservations != null) {
            ArrayList<ReservationDTO> reservationsDTO = new ArrayList<>();
            for (Reservation reservation : reservations) {
                reservationsDTO.add(new ReservationDTO(reservation));
            }

            return new ResponseEntity<Collection<ReservationDTO>>(reservationsDTO, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<ReservationDTO>>(HttpStatus.NOT_FOUND);
    }

    // NOTE: IDK if this method is even needed... if it is, though, Pageable needs to be added!
    // GET "/api/reservations/event/5"
    @GetMapping(value = "/event/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservationsByEventId(@PathVariable Long eventId) {
        ArrayList<Reservation> reservations = reservationService.getReservationsByEventId(eventId);

        if (reservations != null) {
            ArrayList<ReservationDTO> reservationsDTO = new ArrayList<>();
            for (Reservation reservation : reservations) {
                reservationsDTO.add(new ReservationDTO(reservation));
            }

            return new ResponseEntity<Collection<ReservationDTO>>(reservationsDTO, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<ReservationDTO>>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reservations/user/5"
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ReservationDTO>> getReservationsByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<Reservation> reservations = reservationService.getReservationsByUserId(userId, pageable);

        if (reservations != null) {
            ArrayList<ReservationDTO> reservationsDTO = new ArrayList<>();
            for (Reservation reservation : reservations) {
                reservationsDTO.add(new ReservationDTO(reservation));
            }
            long count = reservations.getTotalElements();

            PagedResponse<ReservationDTO> response = new PagedResponse<>(reservationsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
            return new ResponseEntity<PagedResponse<ReservationDTO>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<PagedResponse<ReservationDTO>>(HttpStatus.NOT_FOUND);
    }

    /*
    {
        "selectedEventId": 3,
        "selectedServiceId": 12,
        "reservationStartDateTime": "2024-12-01T14:00:00",
        "reservationEndDateTime": "2024-12-01T16:00:00"
    }
    */
    // POST "/api/reservations"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        Reservation reservation = reservationService.createReservation(reservationDTO);

        if(reservation != null) {
            return new ResponseEntity<ReservationDTO>(new ReservationDTO(reservation), HttpStatus.CREATED);
        }

        return new ResponseEntity<ReservationDTO>(HttpStatus.BAD_REQUEST);
    }
}
