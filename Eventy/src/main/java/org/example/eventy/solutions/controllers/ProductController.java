package org.example.eventy.solutions.controllers;

import org.example.eventy.events.dtos.EventDTO;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.dtos.CreateProductDTO;
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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        ProductDTO productDTO = new ProductDTO();

        if(createProductDTO.getName().equals("product")) {
            productDTO.setId(5L);
            productDTO.setName(createProductDTO.getName());
            productDTO.setPrice(createProductDTO.getPrice());
            productDTO.setDescription(createProductDTO.getDescription());
            productDTO.setAvailability(createProductDTO.isAvailable());
            productDTO.setVisibility(createProductDTO.isVisible());
            productDTO.setImages(createProductDTO.getImages());
            productDTO.setPrice(createProductDTO.getPrice());
            productDTO.setDiscount(createProductDTO.getDiscount());
            productDTO.setRelatedEventTypes(createProductDTO.getRelatedEventTypes());
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.CREATED);
        }

        // validation failed
        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDTO>> getProducts() {
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO(1L, "Product 1", "Product 1 Description", 1.0, 10.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), false, false));
        products.add(new ProductDTO(2L, "Product 2", "Product 2 Description", 2.0, 8.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), true, false));
        products.add(new ProductDTO(3L, "Product 3", "Product 3 Description", 3.0, 5.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), false, true));

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long productId) {
        ProductDTO productDTO = new ProductDTO();
        if(productId.equals(5L)) {
            productDTO.setId(5L);
            productDTO.setName("Product 5");
            productDTO.setPrice(5.0);
            productDTO.setDiscount(7.0);
            productDTO.setDescription("Product 5 Description");
            productDTO.setAvailability(true);
            productDTO.setVisibility(true);
            productDTO.setImages(new ArrayList<String>());
            productDTO.setRelatedEventTypes(new ArrayList<EventTypeDTO>());
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(productDTO, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/provider/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDTO>> getProductsProvider(@PathVariable Long userId) {
        List<ProductDTO> products = new ArrayList<>();
        if(userId == 5) {
            products.add(new ProductDTO(1L, "Product 1", "Product 1 Description", 1.0, 10.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), false, false));
            products.add(new ProductDTO(2L, "Product 2", "Product 2 Description", 2.0, 8.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), true, false));
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId) {
        ProductDTO updatedProductDTO = new ProductDTO();
        if(productDTO.getId() == 5L && productId == 5L) {
            updatedProductDTO.setId(productDTO.getId());
            updatedProductDTO.setName(productDTO.getName());
            updatedProductDTO.setPrice(productDTO.getPrice());
            updatedProductDTO.setDiscount(productDTO.getDiscount());
            updatedProductDTO.setDescription(productDTO.getDescription());
            updatedProductDTO.setAvailability(productDTO.isAvailable());
            updatedProductDTO.setVisibility(productDTO.isVisible());
            updatedProductDTO.setImages(productDTO.getImages());
            updatedProductDTO.setRelatedEventTypes(productDTO.getRelatedEventTypes());
            return new ResponseEntity<ProductDTO>(updatedProductDTO, HttpStatus.OK);
        }

        return new ResponseEntity<ProductDTO>(updatedProductDTO, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{productId}/deletion", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        if(productId == 5) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
