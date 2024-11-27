package org.example.eventy.solutions.services;

import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.SolutionProvider;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

@org.springframework.stereotype.Service
public class ProductService {
    // @Autowired
    // private ProductRepository productRepository;

    public ArrayList<Solution> getProducts(Pageable pageable) {
        ArrayList<Solution> products = generateProductExamples();
        return products;
    }

    public Solution getProduct(Long productId) {
        ArrayList<Solution> products = generateProductExamples();
        Solution product = products.get(0);
        product.setId(productId);

        return product;
    }

    public ArrayList<Solution> generateProductExamples() {
        Category category1 = new Category();
        category1.setName("Catering");
        ArrayList<EventType> eventTypes = new ArrayList<EventType>();
        EventType eventType1 = new EventType();
        eventType1.setName("Wedding");
        EventType eventType2 = new EventType();
        eventType2.setName("Birthday");
        eventTypes.add(eventType1);
        eventTypes.add(eventType2);
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add("https://example.com/solution.png");
        SolutionProvider provider = new SolutionProvider();
        provider.setName("TacTac");
        provider.setEmail("cakes.luxury@gmail.com");
        provider.setImageUrls(imageUrls);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Luxury Wedding Cake");
        product1.setCategory(category1);
        product1.setDescription("A handcrafted 3-tier wedding cake with customizable flavors.");
        product1.setEventTypes(eventTypes);
        product1.setPrice(150.00);
        product1.setDiscount(10);
        product1.setImageUrls(imageUrls);
        product1.setAvailable(true);
        product1.setProvider(provider);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Luxury Bouquet Wedding");
        product2.setCategory(category1);
        product2.setDescription("A handcrafted bouquet with customizable flowers.");
        product2.setEventTypes(eventTypes);
        product2.setPrice(360.00);
        product2.setDiscount(15);
        product2.setImageUrls(imageUrls);
        product2.setAvailable(true);
        product2.setProvider(provider);

        ArrayList<Solution> products = new ArrayList<Solution>();
        products.add(product1);
        products.add(product2);

        return products;
    }
}
