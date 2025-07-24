package org.example.eventy.solutions.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.events.models.Budget;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.services.BudgetItemService;
import org.example.eventy.events.services.BudgetService;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.solutions.dtos.*;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.models.SolutionHistory;
import org.example.eventy.solutions.services.ProductService;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.example.eventy.solutions.services.SolutionHistoryService;
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
    private SolutionHistoryService solutionHistoryService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private BudgetItemService budgetItemService;

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
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductDTO createProductDTO,
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
        product.setVisible(createProductDTO.getIsVisible());
        product.setAvailable(createProductDTO.getIsAvailable());
        product.setDeleted(false);
        product.setProvider((SolutionProvider) user);
        product.setCurrentProduct(solutionHistoryService.save(new SolutionHistory(product)));

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

    // not mine actually, feel free to override
    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long productId) {
        ProductDTO productDTO = new ProductDTO();
        if(productId.equals(5L)) {
            productDTO.setId(5L);
            productDTO.setName("Product 5");
            productDTO.setPrice(5.0);
            productDTO.setDiscount(7.0);
            productDTO.setDescription("Product 5 Description");
            productDTO.setIsAvailable(true);
            productDTO.setIsVisible(true);
            productDTO.setImages(new ArrayList<String>());
            productDTO.setRelatedEventTypes(new ArrayList<EventTypeDTO>());
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(productDTO, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('Provider')")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId) {
        Product product = productService.getProduct(productId);

        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscount((int) productDTO.getDiscount());
        product.setImageUrls(pictureService.save(productDTO.getImages()));
        product.setEventTypes(productDTO.getRelatedEventTypes().stream().map(eventType -> eventTypeService.get(eventType.getId())).collect(Collectors.toList()));
        product.setVisible(productDTO.getIsVisible());
        product.setAvailable(productDTO.getIsAvailable());
        product.setCurrentProduct(solutionHistoryService.save(new SolutionHistory(product)));

        product = productService.save(product);

        if(product == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ProductDTO>(new ProductDTO(product), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Organizer')")
    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionDetailsDTO> purchaseProduct(@RequestBody ProductPurchaseDTO productPurchaseDTO, @RequestHeader(value = "Authorization") String token) {

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
            return new ResponseEntity<SolutionDetailsDTO>(HttpStatus.FORBIDDEN);
        }

        Budget budget = budgetService.getBudget(productPurchaseDTO.getEventId());
        if (budget == null) {
            budget = budgetService.createBudget(productPurchaseDTO.getEventId());
        }

        Product product = productService.getProduct(productPurchaseDTO.getProductId());
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!product.isAvailable()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SolutionHistory productToBuy = product.getCurrentProduct();
        if (productToBuy == null) {
            productToBuy = solutionHistoryService.save(new SolutionHistory(product));
            product.setCurrentProduct(productToBuy);
            productService.save(product);
        }

        BudgetItem budgetItem = budget.getBudgetedItems().stream().filter(v -> v.getCategory() == product.getCategory()).findFirst().orElse(null);
        if (budgetItem == null) {
            budgetItem = budgetItemService.createBudgetItem(product.getCategory(), 0.0);
        }
        budgetItem = budgetItemService.addBudgetItemSolution(budgetItem, productToBuy);

        return new ResponseEntity<SolutionDetailsDTO>(new SolutionDetailsDTO(productToBuy, product, user),HttpStatus.OK);
    }
}
