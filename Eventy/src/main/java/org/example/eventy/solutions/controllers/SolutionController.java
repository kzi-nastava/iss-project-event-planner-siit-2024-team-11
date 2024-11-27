package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.PriceListDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.dtos.SolutionDTO;
import org.example.eventy.solutions.models.Solution;
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
    // GET http://localhost:8080/api/solutions?search=cake&type=SERVICE&category=Wedding&eventTypes=Ceremony,Reception&company=ElegantEvents&minPrice=100&maxPrice=1000&startDate=2024-12-01&endDate=2024-12-31&isAvailable=true&page=0&size=10&sort=name,asc
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getSolutions(
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
        ArrayList<Solution> solutionModels = solutionService.getSolutions(
                search, type, category, eventTypes, company, minPrice, maxPrice, startDate, endDate, isAvailable, pageable);

        ArrayList<SolutionCardDTO> solutions = new ArrayList<>();
        for (Solution solution : solutionModels) {
            solutions.add(new SolutionCardDTO(solution));
        }

        return new ResponseEntity<Collection<SolutionCardDTO>>(solutions, HttpStatus.OK);
    }

    // GET "/api/solutions/featured"
    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getFeaturedSolutions() {
        ArrayList<Solution> featuredSolutionModels = solutionService.getFeaturedSolutions();

        ArrayList<SolutionCardDTO> featuredSolutions = new ArrayList<>();
        for (Solution solution : featuredSolutionModels) {
            featuredSolutions.add(new SolutionCardDTO(solution));
        }

        return new ResponseEntity<Collection<SolutionCardDTO>>(featuredSolutions, HttpStatus.OK);
    }

    // GET "/api/solutions/cards/5"
    @GetMapping(value = "/cards/{solutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getSolutionCard(@PathVariable Long solutionId) {
        if (solutionId == 5) {
            Solution solutionModel = solutionService.getSolution(solutionId);
            SolutionCardDTO solutionCard = new SolutionCardDTO(solutionModel);

            return new ResponseEntity<SolutionCardDTO>(solutionCard, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/pricelist/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getProviderPrices(@PathVariable Long userId) {
        if(userId == 5) {
            // prices are shown in cards like the cards on the homepage
            List<Solution> solutionModels = solutionService.getSolutions(null, null, null, null, null, 0, 99999999, null, null, null, Pageable.unpaged());

            ArrayList<SolutionCardDTO> solutions = new ArrayList<>();
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
}
