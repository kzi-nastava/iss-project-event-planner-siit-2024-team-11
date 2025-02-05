package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.services.*;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceDTO> createService(@RequestBody CreateServiceDTO service) {
        ServiceDTO response = new ServiceDTO(serviceService.createService(service));

        return new ResponseEntity<ServiceDTO>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceDTO> getService(@PathVariable("id") long id) {
        Service service = (Service) serviceService.getService(id);
        if (service == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ServiceDTO(service), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceDTO> updateService(@RequestBody UpdateServiceDTO service) {
        Service updatedService = serviceService.updateService(service);
        if (updatedService == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ServiceDTO(updatedService), HttpStatus.OK);
    }

    // GET "/api/services/cards/5"
    @GetMapping(value = "/cards/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getServiceCard(@PathVariable Long serviceId, @RequestHeader(value = "Authorization", required = false) String token) {
        Solution service = serviceService.getService(serviceId);

        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (service != null) {
            SolutionCardDTO serviceCardDTO = new SolutionCardDTO(service, user);
            return new ResponseEntity<SolutionCardDTO>(serviceCardDTO, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }
}
