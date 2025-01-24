package org.example.eventy.solutions.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.solutions.dtos.PriceListDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ProductService;
import org.example.eventy.solutions.services.ServiceService;
import org.example.eventy.solutions.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;
    private ServiceService serviceService;
    private ProductService productService;

    @GetMapping(value = "/favorite/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getFavoriteSolutions(@PathVariable Long userId, @RequestParam(required = false) String search,
                                                                        Pageable pageable) {
        List<Solution> providerSolutions = solutionService.getFavoriteSolutionsByUser(userId, search, pageable);

        List<SolutionCardDTO> solutionCards = providerSolutions.stream().map(SolutionCardDTO::new).collect(Collectors.toList());
        long count = solutionService.getFavoriteSolutionsByUserCount(userId);
        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(solutionCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/catalog/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getProviderCatalog(@PathVariable Long userId, @RequestParam(required = false) String search,
                                                                             Pageable pageable) {
        List<Solution> providerSolutions = solutionService.getSolutionsByProvider(userId, search, pageable);

        List<SolutionCardDTO> solutionCards = providerSolutions.stream().map(SolutionCardDTO::new).collect(Collectors.toList());
        long count = solutionService.getSolutionsByProviderCount(userId);
        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(solutionCards, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    /* this returns SolutionCardDTOs, because there is NO CASE where:
       1) we need ALL solution
       2) they are NOT in card shapes (they always will be if we are getting all solution) */
    // GET http://localhost:8080/api/solutions?search=party&type=Service&categories=Music,Decoration&eventTypes=Wedding,Birthday&company=EventCorp&minPrice=100&maxPrice=5000&startDate=2024-12-20T10:00:00&endDate=2024-12-20T20:00:00&isAvailable=true&page=0&size=10&sort=name,asc
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<SolutionCardDTO>> getSolutions(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "Any") String type,
            @RequestParam(required = false) ArrayList<String> categories,
            @RequestParam(required = false) ArrayList<String> eventTypes,
            @RequestParam(required = false, defaultValue = "") String company,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "99999999") Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
            Pageable pageable) {
        // Pageable - page, size, sort
        // sort by: "category", "name", "price,asc", "price,desc"
        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0); // Very small date
        }
        if (endDate == null) {
            endDate = LocalDateTime.of(2099, 12, 31, 23, 59); // Very large date
        }

        StringBuilder eventTypesConcatenated = new StringBuilder();
        if (eventTypes != null && !eventTypes.isEmpty()) {
            for (String et : eventTypes) {
                eventTypesConcatenated.append(et).append(",");
            }
            eventTypesConcatenated.deleteCharAt(eventTypesConcatenated.length() - 1);
        }

        StringBuilder categories2 = new StringBuilder();
        if (categories != null) {
            for (String c : categories) {
                categories2.append(c).append(",");
            }
            categories2.deleteCharAt(categories2.length() - 1);
        }

        Page<Solution> solutions = solutionService.getSolutions(
            search, type, categories2.toString(), eventTypesConcatenated.toString(), company, minPrice, maxPrice, startDate, endDate, isAvailable, pageable);

        List<SolutionCardDTO> solutionCardsDTO = new ArrayList<>();
        for (Solution solution : solutions) {
            solutionCardsDTO.add(new SolutionCardDTO(solution));
        }
        long count = solutions.getTotalElements();

        PagedResponse<SolutionCardDTO> response = new PagedResponse<>(solutionCardsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<SolutionCardDTO>>(response, HttpStatus.OK);
    }

    // GET "/api/solutions/cards/5"
    @GetMapping(value = "/cards/{solutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getSolutionCard(@PathVariable Long solutionId) {
        Solution solution = solutionService.getSolution(solutionId);

        if (solution != null) {
            SolutionCardDTO solutionCard = new SolutionCardDTO(solution);
            return new ResponseEntity<SolutionCardDTO>(solutionCard, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/solutions/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getFeaturedSolutions() {
        ArrayList<Solution> featuredSolutions = solutionService.getFeaturedSolutions();

        ArrayList<SolutionCardDTO> featuredSolutionsDTO = new ArrayList<>();
        for (Solution solution : featuredSolutions) {
            featuredSolutionsDTO.add(new SolutionCardDTO(solution));
        }

        return new ResponseEntity<Collection<SolutionCardDTO>>(featuredSolutionsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/pricelist/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getProviderPrices(@PathVariable Long userId) {
        if(userId == 5) {
            // prices are shown in cards like the cards on the homepage
            Page<Solution> solutionModels = solutionService.getSolutions(null, null, null, null, null, 0, 99999999, null, null, null, Pageable.unpaged());

            List<SolutionCardDTO> solutions = new ArrayList<>();
            for (Solution solution : solutionModels) {
                solutions.add(new SolutionCardDTO(solution));
            }

            return new ResponseEntity<Collection<SolutionCardDTO>>(solutions, HttpStatus.OK);
        }

        return new ResponseEntity<Collection<SolutionCardDTO>>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/pricelist/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProviderPrices(@PathVariable Long userId, @RequestBody PriceListDTO newPricelist) {
        if(userId == 5) {
            // handle new price list in service
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/event-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueEventTypesForSolutions() {
        ArrayList<String> eventTypeNames = solutionService.getAllUniqueEventTypesForSolutions();
        return new ResponseEntity<Collection<String>>(eventTypeNames, HttpStatus.OK);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueCategoriesForSolutions() {
        ArrayList<String> categoryNames = solutionService.getAllUniqueCategoriesForSolutions();
        return new ResponseEntity<Collection<String>>(categoryNames, HttpStatus.OK);
    }

    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<String>> getAllUniqueCompaniesForSolutions() {
        ArrayList<String> companyNames = solutionService.getAllUniqueCompaniesForSolutions();
        return new ResponseEntity<Collection<String>>(companyNames, HttpStatus.OK);
    }
}
