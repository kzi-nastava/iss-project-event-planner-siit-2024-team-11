package org.example.eventy.solutions.controllers;

import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.solutions.dtos.CreateProductDTO;
import org.example.eventy.solutions.dtos.ProductDTO;
import org.example.eventy.solutions.dtos.ProductPurchaseDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.ProductHistory;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.ProductHistoryService;
import org.example.eventy.solutions.services.ProductService;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.example.eventy.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private SolutionCategoryService solutionCategoryService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private ProductHistoryService productHistoryService;

    // GET "/api/products/cards/5"
    @GetMapping(value = "/cards/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionCardDTO> getProductCard(@PathVariable Long productId, @RequestHeader(value = "Authorization", required = false) String token) {
        Solution product = productService.getProduct(productId);

        User user = null;
        if(token != null) {
            token = token.substring(7);

            try {
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }
        if (product != null) {
            SolutionCardDTO productCardDTO = new SolutionCardDTO(product, user);
            return new ResponseEntity<SolutionCardDTO>(productCardDTO, HttpStatus.OK);
        }

        return new ResponseEntity<SolutionCardDTO>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDTO createProductDTO,
                                                    @RequestHeader(value = "Authorization", required = false) String token) {
        Product product = new Product();

        User user = null;
        if(token != null) {
            try {
                token = token.substring(7);
                user = userService.findByEmail(tokenUtils.getUsernameFromToken(token));
            }
            catch (Exception ignored) {
            }
        }

        if (user == null) {
            return new ResponseEntity<ProductDTO>(HttpStatus.FORBIDDEN);
        }

        product.setName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setPrice(createProductDTO.getPrice());
        product.setDiscount(createProductDTO.getDiscount());
        product.setImageUrls(pictureService.save(createProductDTO.getImageUrls()));
        product.setCategory(solutionCategoryService.getCategory(createProductDTO.getCategory().getId()));
        product.setEventTypes(createProductDTO.getRelatedEventTypes().stream().map(eventType -> eventTypeService.get(eventType.getId())).collect(Collectors.toList()));
        product.setVisible(createProductDTO.isVisible());
        product.setAvailable(createProductDTO.isAvailable());
        product.setDeleted(false);
        product.setProvider((SolutionProvider) user);
        product.setCurrentProduct(productHistoryService.save(new ProductHistory(product)));

        product = productService.save(product);

        if(product == null) {
            return new ResponseEntity<ProductDTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ProductDTO>(new ProductDTO(product), HttpStatus.CREATED);
    }

    /*
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDTO>> getProducts() {
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO(1L, "Product 1", "Product 1 Description", 1.0, 10.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), false, false));
        products.add(new ProductDTO(2L, "Product 2", "Product 2 Description", 2.0, 8.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), true, false));
        products.add(new ProductDTO(3L, "Product 3", "Product 3 Description", 3.0, 5.0, new ArrayList<EventTypeDTO>(), new ArrayList<String>(), false, true));

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
*/
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

        if(productId == 100L) {
            return new ResponseEntity<ProductDTO>(updatedProductDTO, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ProductDTO>(updatedProductDTO, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        if(productId == 5) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> purchaseProduct(@RequestBody ProductPurchaseDTO productPurchaseDTO) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
