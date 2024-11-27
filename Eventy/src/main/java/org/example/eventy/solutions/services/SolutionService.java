package org.example.eventy.solutions.services;

import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.SolutionProvider;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.ArrayList;

@org.springframework.stereotype.Service
public class SolutionService {
    /*@Autowired
    // will be changed probably..
    private SolutionRepository solutionRepository;*/

    public ArrayList<Solution> getSolutions(String search, String type, String category, ArrayList<String> eventTypes, String company, double minPrice, double maxPrice, LocalDate startDate, LocalDate endDate, Boolean isAvailable, Pageable pageable) {
        ArrayList<Solution> solutions = generateSolutionExamples(1);
        return solutions;
        //return solutionRepository.findFilteredSolutions(search, type, category, eventTypes, company, minPrice, maxPrice, startDate, endDate, isAvailable, pageable);
    }

    public ArrayList<Solution> getFeaturedSolutions() {
        ArrayList<Solution> featuredSolutions = generateSolutionExamples(2);
        return featuredSolutions;
    }

    public Solution getSolution(Long solutionId) {
        ArrayList<Solution> solutions = generateSolutionExamples(1);
        Solution solution = solutions.get(0);
        solution.setId(solutionId);

        return solution;
    }

    public ArrayList<Solution> generateSolutionExamples(int type) {
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
        product1.setName(type == 1 ? "Luxury Wedding Cake" : "FEATURED - Luxury Wedding Cake");
        product1.setCategory(category1);
        product1.setDescription("A handcrafted 3-tier wedding cake with customizable flavors.");
        product1.setEventTypes(eventTypes);
        product1.setPrice(150.00);
        product1.setDiscount(10);
        product1.setImageUrls(imageUrls);
        product1.setAvailable(true);
        product1.setProvider(provider);

        Category category2 = new Category();
        category2.setName("Entertainment");
        provider.setEmail("exit.festival@gmail.com");

        Service service1 = new Service();
        service1.setId(2L);
        service1.setName(type == 1 ? "DJ Services" : "FEATURED - DJ Services");
        service1.setCategory(category2);
        service1.setMinReservationTime(2);
        service1.setMaxReservationTime(6);
        service1.setDescription(null);
        service1.setEventTypes(eventTypes);
        service1.setPrice(150.00);
        service1.setDiscount(10);
        service1.setImageUrls(imageUrls);
        service1.setAvailable(true);
        service1.setProvider(provider);

        ArrayList<Solution> solutions = new ArrayList<Solution>();
        solutions.add(product1);
        solutions.add(service1);

        return solutions;
    }
}
