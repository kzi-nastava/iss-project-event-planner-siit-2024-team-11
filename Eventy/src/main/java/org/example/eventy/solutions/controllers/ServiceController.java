package org.example.eventy.solutions.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.solutions.dtos.services.*;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.ArrayList;

@RestController
@RequestMapping("api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedServiceDTO> createService(@RequestBody CreateServiceDTO service) {
        CreatedServiceDTO response = serviceService.createService(service);

        return new ResponseEntity<CreatedServiceDTO>(response, HttpStatus.CREATED);
    }

    // NOTE: Add extra RequestParams as necessary if more filters are needed
    /*@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetServiceDTO>> getServices(@RequestParam (required = false) String name,
                                                            @RequestParam (required = false) CategoryDTO category,
                                                            @RequestParam (required = false) EventTypeDTO eventType,
                                                            @RequestParam (required = false) double minPrice,
                                                            @RequestParam (required = false) double maxPrice,
                                                            @RequestParam (required = false) boolean available) {
        Collection<GetServiceDTO> services = serviceService.getServices(name, category, eventType, minPrice, maxPrice, available);

        return new ResponseEntity<>(services, HttpStatus.OK);
    }*/

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetServiceDTO> getService(@PathVariable("id") long id) {
        /*Optional<GetServiceDTO> service = serviceService.getService(id);
        if (!service.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/
        GetServiceDTO service = new GetServiceDTO();
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedServiceDTO> updateService(@RequestBody UpdateServiceDTO service, @PathVariable("id") long id) {
        service.setId(id);
        Optional<UpdatedServiceDTO> updatedService = serviceService.updateService(service);
        if (!updatedService.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedService.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") long id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* this returns SolutionCardDTOs, because there is NO CASE where:
       1) we need ALL services
       2) they are NOT in card shapes (they always will be if we are getting all services)
       also SolutionCardDTO == ProductCardDTO == ServiceCardDTO (only a few of fields will be null) */
    // GET "/api/services"
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getServices(Pageable pageable) {
        Page<Solution> services = serviceService.getServices(pageable);

        ArrayList<SolutionCardDTO> servicesDTO = new ArrayList<>();
        for (Solution service : services) {
            servicesDTO.add(new SolutionCardDTO(service));
        }
        long count = services.getTotalElements();

        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(servicesDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    // GET "/api/services/cards/5"
    @GetMapping(value = "/cards/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getServiceCard(@PathVariable Long serviceId) {
        Solution service = serviceService.getService(serviceId);

        if (service != null) {
            SolutionCardDTO serviceCardDTO = new SolutionCardDTO(service);
            return new ResponseEntity<SolutionCardDTO>(serviceCardDTO, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }
}
