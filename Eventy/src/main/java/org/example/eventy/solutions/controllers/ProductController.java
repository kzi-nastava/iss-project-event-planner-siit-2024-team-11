package org.example.eventy.solutions.controllers;

import org.example.eventy.solutions.dtos.ProductDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /* this returns SolutionCardDTOs, because there is NO CASE where:
       1) we need ALL products
       2) they are NOT in card shapes (they always will be if we are getting all products)
       also SolutionCardDTO == ProductCardDTO == ServiceCardDTO (only a few of fields will be null) */
    // GET "/api/products"
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<SolutionCardDTO>> getProducts(Pageable pageable) {
        ArrayList<SolutionCardDTO> products = productService.getProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // GET "/api/products/5/card"
    @GetMapping(value = "/{productId}/card", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getProductCard(@PathVariable Long productId) {
        SolutionCardDTO productCard = productService.getProductCard(productId);

        if (productId == 5) {
            return new ResponseEntity<SolutionCardDTO>(productCard, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        if(productDTO.getName().equals("product")) {
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.CREATED);
        }

        return new ResponseEntity<ProductDTO>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/provider/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDTO>> getProductsProvider(@PathVariable Long userId) {
        if(userId == 5) {
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

    @PutMapping(value = "{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        if(productId == 5) {
            return new ResponseEntity<ProductDTO>(HttpStatus.OK);
        }

        return new ResponseEntity<ProductDTO>(HttpStatus.NOT_FOUND);
    }
}
