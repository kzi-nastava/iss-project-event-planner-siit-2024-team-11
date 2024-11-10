package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        if(productDTO.getName().equals("product")) {
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.CREATED);
        }

        return new ResponseEntity<ProductDTO>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDTO>> getProducts() {
        List<ProductDTO> products = new ArrayList<>();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/provider/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDTO>> getProductsProvider(@PathVariable("id") Long id) {
        if(id == 5) {
            List<ProductDTO> products = new ArrayList<>();
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
        if(productDTO.getName().equals("product")) {
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
        }

        return new ResponseEntity<ProductDTO>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> deleteProduct(@RequestBody ProductDTO productDTO) {
        if(productDTO.getName().equals("product")) {
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
        }

        return new ResponseEntity<ProductDTO>(HttpStatus.NOT_FOUND);
    }
}
