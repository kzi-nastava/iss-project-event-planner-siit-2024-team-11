package org.example.eventy.solutions.controllers;

import org.example.eventy.events.dtos.EventCardDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.dtos.SolutionDTO;
import org.example.eventy.solutions.services.ProductService;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.solutions.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;
    private ServiceService serviceService;
    private ProductService productService;

    @GetMapping(value = "/favorite/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionDTO>> getFavoriteSolutions(@PathVariable Long userId) {
        if(userId == 5) {
            List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
            return new ResponseEntity<Collection<SolutionDTO>>(solutions, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<SolutionDTO>>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/catalog/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionDTO>> getProviderCatalog(@PathVariable Long userId) {
        if(userId == 5) {
            List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
            return new ResponseEntity<Collection<SolutionDTO>>(solutions, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<SolutionDTO>>(HttpStatus.NOT_FOUND);
    }

    /* this returns SolutionCardDTOs, because there is NO CASE where:
       1) we need ALL solution
       2) they are NOT in card shapes (they always will be if we are getting all solution) */
    // GET "/api/solutions"
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getSolutions(Pageable pageable) {
        ArrayList<SolutionCardDTO> solutions = solutionService.getSolutions(pageable);
        return new ResponseEntity<Collection<SolutionCardDTO>>(solutions, HttpStatus.OK);
    }

    // GET "/api/solutions/5/card"
    @GetMapping(value = "/{solutionId}/card", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getSolutionCard(@PathVariable Long solutionId) {
        SolutionCardDTO solutionCard = solutionService.getSolutionCard(solutionId);

        if (solutionId == 5) {
            return new ResponseEntity<SolutionCardDTO>(solutionCard, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/solutions/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getFeaturedSolutions() {
        ArrayList<SolutionCardDTO> featuredSolutions = solutionService.getFeaturedSolutions();
        return new ResponseEntity<Collection<SolutionCardDTO>>(featuredSolutions, HttpStatus.OK);
    }

    // GET http://localhost:8080/api/solutions/filter?search=cake&type=SERVICE&category=Wedding&eventTypes=Ceremony,Reception&company=ElegantEvents&minPrice=100&maxPrice=1000&startDate=2024-12-01&endDate=2024-12-31&isAvailable=true&page=0&size=10&sort=name,asc
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> filterSolutions(
        @RequestParam(required = false) String search,
        @RequestParam(required = false, defaultValue = "Any") String type,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) ArrayList<String> eventTypes,
        @RequestParam(required = false) String company,
        @RequestParam(required = false, defaultValue = "0") Double minPrice,
        @RequestParam(required = false, defaultValue = "99999999") Double maxPrice,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
        Pageable pageable) {
        // Pageable - page, size, sort
        // sort by: "category", "name", "price,asc", "price,desc", "date,asc", "date,desc", "duration,asc", "duration,desc"
        ArrayList<SolutionCardDTO> filteredSolutions = solutionService.filterSolutions(
                search, type, category, eventTypes, company, minPrice, maxPrice, startDate, endDate, isAvailable, pageable);

        return new ResponseEntity<Collection<SolutionCardDTO>>(filteredSolutions, HttpStatus.OK);
    }
}
