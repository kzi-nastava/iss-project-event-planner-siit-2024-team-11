package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.ServiceReservationDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.services.ServiceReservationService;
import org.example.eventy.solutions.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("api/reservations")
public class ServiceReservationController {

    @Autowired
    private ServiceReservationService serviceReservationService;

    /*
    {
        "id": 5,
        "selectedEventId": 3,
        "selectedSolutionId": 12,
        "reservationStartDateTime": "2024-12-01T14:00:00",
        "reservationEndDateTime": "2024-12-01T16:00:00"
    }
    */
    // POST "/api/reservations"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceReservationDTO> createServiceReservation(@RequestBody ServiceReservationDTO serviceReservation) {
        if (serviceReservation.getId() == 5) {
            ServiceReservationDTO newServiceReservation = serviceReservationService.createServiceReservation(serviceReservation);
            return new ResponseEntity<>(newServiceReservation, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // GET "/api/reservations/5"
    @GetMapping(value = "/{serviceReservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceReservationDTO> getServiceReservation(@PathVariable Long serviceReservationId) {
        ServiceReservationDTO serviceReservation = serviceReservationService.getServiceReservation(serviceReservationId);

        if (serviceReservationId == 5) {
            return new ResponseEntity<ServiceReservationDTO>(serviceReservation, HttpStatus.OK);
        }

        return new ResponseEntity<ServiceReservationDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reservations/service/5"
    @GetMapping(value = "/service/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ServiceReservationDTO>> getReservationsByServiceId(@PathVariable Long serviceId) {
        ArrayList<ServiceReservationDTO> serviceReservations = serviceReservationService.getReservationsByServiceId(serviceId);

        if (serviceId == 5) {
            return new ResponseEntity<Collection<ServiceReservationDTO>>(serviceReservations, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<ServiceReservationDTO>>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reservations/event/5"
    @GetMapping(value = "/event/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ServiceReservationDTO>> getReservationsByEventId(@PathVariable Long eventId) {
        ArrayList<ServiceReservationDTO> serviceReservations = serviceReservationService.getReservationsByEventId(eventId);

        if (eventId == 5) {
            return new ResponseEntity<Collection<ServiceReservationDTO>>(serviceReservations, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<ServiceReservationDTO>>(HttpStatus.NOT_FOUND);
    }
}
