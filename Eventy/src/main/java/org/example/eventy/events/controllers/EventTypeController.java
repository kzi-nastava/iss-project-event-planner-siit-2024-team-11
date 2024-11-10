package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.EventTypeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/events/types")
public class EventTypeController {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventTypeDTO>> getAllTypes(Pageable pageable) {
        List<EventTypeDTO> eventTypes = new ArrayList<EventTypeDTO>();
        return new ResponseEntity<Collection<EventTypeDTO>>(eventTypes, HttpStatus.OK);
    }

    @GetMapping(value ="/search/{searchText}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventTypeDTO>> getFilteredTypes(@PathVariable String searchText, Pageable pageable) {
        List<EventTypeDTO> eventTypes = new ArrayList<EventTypeDTO>();
        return new ResponseEntity<Collection<EventTypeDTO>>(eventTypes, HttpStatus.OK);
    }

    @GetMapping(value = "/{eventTypeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeDTO> getTypeById(@PathVariable Long eventTypeId) {
        if(eventTypeId == 5) {
            EventTypeDTO eventType = new EventTypeDTO();
            return new ResponseEntity<EventTypeDTO>(eventType, HttpStatus.OK);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeDTO> createType(@RequestBody EventTypeDTO eventTypeDTO) {
        if(eventTypeDTO.getName().equals("tip")) {
            return new ResponseEntity<EventTypeDTO>(eventTypeDTO, HttpStatus.CREATED);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeDTO> updateType(@RequestBody EventTypeDTO eventTypeDTO) {
        if(eventTypeDTO.getName().equals("tip")) {
            return new ResponseEntity<EventTypeDTO>(eventTypeDTO, HttpStatus.OK);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{eventTypeId}")
    public ResponseEntity<EventTypeDTO> deactivateType(@PathVariable Long eventTypeId) {
        if(eventTypeId == 5) {
            return new ResponseEntity<EventTypeDTO>(HttpStatus.OK);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.NOT_FOUND);
    }
}
