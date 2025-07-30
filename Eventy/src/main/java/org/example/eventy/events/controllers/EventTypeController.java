package org.example.eventy.events.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.events.dtos.*;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/events/types")
public class EventTypeController {
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private SolutionCategoryService solutionCategoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<PagedResponse<EventTypeCardDTO>> getTypes(@RequestParam(required = false, defaultValue = "") String search, Pageable pageable) {
        List<EventType> eventTypes = eventTypeService.getTypes(search, pageable);
        List<EventTypeCardDTO> eventTypeDTOs = new ArrayList<EventTypeCardDTO>();

        for(EventType eventType : eventTypes) {
            eventTypeDTOs.add(new EventTypeCardDTO(eventType));
        }

        long count = eventTypeService.getCount(search);

        PagedResponse<EventTypeCardDTO> pagedResponse = new PagedResponse<>(eventTypeDTOs, (int) Math.ceil((double) count / pageable.getPageSize()), count);

        return new ResponseEntity<PagedResponse<EventTypeCardDTO>>(pagedResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Organizer') or hasRole('Provider')")
    public ResponseEntity<Collection<EventTypeCardDTO>> getActiveTypes() {
        List<EventType> eventTypes = eventTypeService.getActiveTypes();
        List<EventTypeCardDTO> eventTypeDTOs = new ArrayList<EventTypeCardDTO>();

        for(EventType eventType : eventTypes) {
            eventTypeDTOs.add(new EventTypeCardDTO(eventType));
        }

        return new ResponseEntity<Collection<EventTypeCardDTO>>(eventTypeDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{eventTypeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventTypeWithActivityDTO> getType(@PathVariable Long eventTypeId) {
        EventType eventType = eventTypeService.get(eventTypeId);

        if(eventType != null) {
            EventTypeWithActivityDTO eventTypeDTO = new EventTypeWithActivityDTO(eventType);
            return new ResponseEntity<EventTypeWithActivityDTO>(eventTypeDTO, HttpStatus.OK);
        }

        return new ResponseEntity<EventTypeWithActivityDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventTypeDTO> createType(@Valid @RequestBody CreatedEventTypeDTO createdEventTypeDTO) {
        EventType eventType = new EventType();
        eventType.setName(createdEventTypeDTO.getName());
        eventType.setDescription(createdEventTypeDTO.getDescription());
        eventType.setActive(true);
        Set<Category> recommendedSolutionCategories = new HashSet<>();
        for(Long id : createdEventTypeDTO.getRecommendedSolutionCategoriesIds()) {
            Category category = solutionCategoryService.getCategory(id);
            recommendedSolutionCategories.add(category);
        }
        eventType.setRecommendedSolutionCategories(recommendedSolutionCategories);

        eventType = eventTypeService.save(eventType);

        if(eventType != null) {
            return new ResponseEntity<EventTypeDTO>(new EventTypeDTO(eventType), HttpStatus.CREATED);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventTypeDTO> updateType(@Valid @RequestBody UpdateEventTypeDTO updateEventTypeDTO) {
        EventType eventType = eventTypeService.get(updateEventTypeDTO.getId());
        if(eventType == null) {
            return new ResponseEntity<EventTypeDTO>(HttpStatus.NOT_FOUND);
        }

        eventType.setName(updateEventTypeDTO.getName());
        eventType.setDescription(updateEventTypeDTO.getDescription());
        Set<Category> recommendedSolutionCategories = new HashSet<>();
        for(Long id : updateEventTypeDTO.getRecommendedSolutionCategoriesIds()) {
            Category category = solutionCategoryService.getCategory(id);
            recommendedSolutionCategories.add(category);
        }

        eventType.setRecommendedSolutionCategories(recommendedSolutionCategories);

        eventType = eventTypeService.save(eventType);

        if(eventType != null) {
            return new ResponseEntity<EventTypeDTO>(new EventTypeDTO(eventType), HttpStatus.OK);
        }

        return new ResponseEntity<EventTypeDTO>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{eventTypeId}/activation", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventTypeWithActivityDTO> toggleActivate(@PathVariable Long eventTypeId) {
        EventType eventType = eventTypeService.toggleActivation(eventTypeId);
        if(eventType != null) {
            return new ResponseEntity<>(new EventTypeWithActivityDTO(eventType), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
