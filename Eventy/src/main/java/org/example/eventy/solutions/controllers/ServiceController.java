package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    /* this returns SolutionCardDTOs, because there is NO CASE where:
       1) we need ALL services
       2) they are NOT in card shapes (they always will be if we are getting all services)
       also SolutionCardDTO == ProductCardDTO == ServiceCardDTO (only a few of fields will be null) */
    // GET "/api/services"
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getServices(Pageable pageable) {
        ArrayList<SolutionCardDTO> services = serviceService.getServices(pageable);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // GET "/api/services/5/card"
    @GetMapping(value = "/{serviceId}/card", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getServiceCard(@PathVariable Long serviceId) {
        SolutionCardDTO serviceCard = serviceService.getServiceCard(serviceId);

        if (serviceId == 5) {
            return new ResponseEntity<SolutionCardDTO>(serviceCard, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }
}
