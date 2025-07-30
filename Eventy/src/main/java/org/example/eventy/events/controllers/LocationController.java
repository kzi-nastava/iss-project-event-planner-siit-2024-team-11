package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.*;
import org.example.eventy.events.models.*;
import org.example.eventy.events.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping(value = "/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDTO> getLocation(@PathVariable Long locationId) {
        Location location = locationService.getLocation(locationId);

        if (location != null) {
            LocationDTO locationDTO = new LocationDTO(location);
            return new ResponseEntity<LocationDTO>(locationDTO, HttpStatus.OK);
        }

        return new ResponseEntity<LocationDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<LocationDTO>> getLocations() {
        List<Location> locations = locationService.getLocations();
        List<LocationDTO> locationsDTO = new ArrayList<LocationDTO>();

        for(Location location : locations) {
            locationsDTO.add(new LocationDTO(location));
        }

        return new ResponseEntity<Collection<LocationDTO>>(locationsDTO, HttpStatus.OK);
    }
}

