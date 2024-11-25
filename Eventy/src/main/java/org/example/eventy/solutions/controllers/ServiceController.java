package org.example.eventy.solutions.controllers;

import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.dtos.CategoryDTO;
import org.example.eventy.solutions.dtos.services.*;
import org.example.eventy.solutions.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("api/services")
public class ServiceController {

    private ServicesService servicesService;

    public ServiceController() {
        servicesService = new ServicesService();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedServiceDTO> createService(@RequestBody CreateServiceDTO service) {
        CreatedServiceDTO response = servicesService.createService(service);

        return new ResponseEntity<CreatedServiceDTO>(response, HttpStatus.CREATED);
    }

    // NOTE: Add extra RequestParams as necessary if more filters are needed
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetServiceDTO>> getServices(@RequestParam (required = false) String name,
                                                            @RequestParam (required = false) CategoryDTO category,
                                                            @RequestParam (required = false) EventTypeDTO eventType,
                                                            @RequestParam (required = false) double minPrice,
                                                            @RequestParam (required = false) double maxPrice,
                                                            @RequestParam (required = false) boolean available) {
        Collection<GetServiceDTO> services = servicesService.getServices(name, category, eventType, minPrice, maxPrice, available);

        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetServiceDTO> getService(@PathVariable("id") long id) {
        Optional<GetServiceDTO> service = servicesService.getService(id);
        if (!service.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(service.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedServiceDTO> updateService(@RequestBody UpdateServiceDTO service, @PathVariable("id") long id) {
        service.setId(id);
        Optional<UpdatedServiceDTO> updatedService = servicesService.updateService(service);
        if (!updatedService.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedService.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") long id) {
        servicesService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
