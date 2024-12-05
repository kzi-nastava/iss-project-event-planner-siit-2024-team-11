package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.CreatedEventTypeDTO;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.events.dtos.UpdateEventTypeDTO;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/events/types")
public class EventTypeController {
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private SolutionCategoryService solutionCategoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<EventTypeDTO>> getypes(@RequestParam(required = false) String search, Pageable pageable) {
        List<EventType> eventTypes = eventTypeService.getTypes(search, pageable);
        List<EventTypeDTO> eventTypeDTOs = new ArrayList<EventTypeDTO>();

        for(EventType eventType : eventTypes) {
            eventTypeDTOs.add(new EventTypeDTO(eventType));
        }
        return new ResponseEntity<Collection<EventTypeDTO>>(eventTypeDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{eventTypeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeDTO> getTypeById(@PathVariable Long eventTypeId) {
        EventType eventType = eventTypeService.get(eventTypeId);

        if(eventTypeId != null) {
            EventTypeDTO eventTypeDTO = new EventTypeDTO(eventType);
            return new ResponseEntity<EventTypeDTO>(eventTypeDTO, HttpStatus.OK);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeDTO> createType(@RequestBody CreatedEventTypeDTO createdEventTypeDTO) {
        EventType eventType = new EventType();
        eventType.setName(createdEventTypeDTO.getName());
        eventType.setDescription(createdEventTypeDTO.getDescription());
        Set<Category> recommendedSolutionCategories = eventType.getRecommendedSolutionCategories();
        for(Long id : createdEventTypeDTO.getRecommendedSolutionCategoriesIds()) {
            Category category = solutionCategoryService.getCategory(id);
        }

        eventType = eventTypeService.save(eventType);

        if(eventType != null) {
            return new ResponseEntity<EventTypeDTO>(new EventTypeDTO(eventType), HttpStatus.CREATED);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventTypeDTO> updateType(@RequestBody UpdateEventTypeDTO updateEventTypeDTO) {
        EventType eventType = new EventType();
        eventType.setName(updateEventTypeDTO.getName());
        eventType.setDescription(updateEventTypeDTO.getDescription());
        Set<Category> recommendedSolutionCategories = eventType.getRecommendedSolutionCategories();
        for(Long id : updateEventTypeDTO.getRecommendedSolutionCategoriesIds()) {
            Category category = solutionCategoryService.getCategory(id);
        }

        eventType = eventTypeService.save(eventType);

        if(eventType != null) {
            return new ResponseEntity<EventTypeDTO>(new EventTypeDTO(eventType), HttpStatus.CREATED);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{eventTypeId}")
    public ResponseEntity<?> deactivateType(@PathVariable Long eventTypeId) {
        if(eventTypeService.deactivate(eventTypeId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
