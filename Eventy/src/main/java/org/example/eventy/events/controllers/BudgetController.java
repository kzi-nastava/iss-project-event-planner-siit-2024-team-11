package org.example.eventy.events.controllers;

import org.example.eventy.events.dtos.BudgetDTO;
import org.example.eventy.events.dtos.BudgetUpdateDTO;
import org.example.eventy.events.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetDTO> createBudget(@PathVariable Long eventId) {
        return new ResponseEntity<>(budgetService.createBudget(eventId), HttpStatus.CREATED);
    }


    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable Long eventId) {
        return new ResponseEntity<>(budgetService.getBudget(eventId), HttpStatus.OK);
    }

    @PutMapping(value = "/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable Long eventId, @RequestBody BudgetUpdateDTO budgetDTO) {
        return new ResponseEntity<>(budgetService.updateBudget(eventId, budgetDTO), HttpStatus.OK);
    }
}
