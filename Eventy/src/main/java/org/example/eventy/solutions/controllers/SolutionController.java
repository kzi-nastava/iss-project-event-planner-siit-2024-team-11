package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.SolutionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/solutions")
public class SolutionController {
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
}
