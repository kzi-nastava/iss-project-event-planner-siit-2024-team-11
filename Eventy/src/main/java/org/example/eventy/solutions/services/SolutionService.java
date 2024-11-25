package org.example.eventy.solutions.services;

import org.example.eventy.common.models.SolutionType;
import org.example.eventy.events.dtos.EventCardDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SolutionService {
    /*@Autowired
    // will be changed probably..
    private SolutionRepository solutionRepository;*/
    private ServiceService serviceService;
    private ProductService productService;

    public ArrayList<SolutionCardDTO> getSolutions(Pageable pageable) {
        SolutionCardDTO solutionCard1 = new SolutionCardDTO(
            1L,
            SolutionType.PRODUCT,
            "Luxury Wedding Cake",
            "Catering",
            "A handcrafted 3-tier wedding cake with customizable flavors.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(List.of("Wedding", "Birthday")),
            150.00,
            10,
            "https://example.com/images/wedding-cake.png",
            true,
            20L,
            "Gourmet Bakers",
            "https://example.com/images/gourmet-bakers.png"
        );

        SolutionCardDTO solutionCard2 = new SolutionCardDTO(
            2L,
            SolutionType.SERVICE,
            "DJ Services",
            "Entertainment",
            null, // descripiton - only for Product
            2,
            6,
            new ArrayList<>(List.of("Wedding", "Corporate")),
            500.00,
            15,
            "https://example.com/images/dj-service.png",
            true,
            25L,
            "Party Vibes Inc.",
            "https://example.com/images/party-vibes.png"
        );

        ArrayList<SolutionCardDTO> solutions = new ArrayList<>();
        solutions.add(solutionCard1);
        solutions.add(solutionCard2);

        return solutions;
    }

    public SolutionCardDTO getSolutionCard(Long solutionId) {
        SolutionCardDTO solutionCard = new SolutionCardDTO(
            solutionId,
            SolutionType.PRODUCT,
            "Luxury Wedding Cake",
            "Catering",
            "A handcrafted 3-tier wedding cake with customizable flavors.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(List.of("Wedding", "Birthday")),
            150.00,
            10,
            "https://example.com/images/wedding-cake.png",
            true,
            20L,
            "Gourmet Bakers",
            "https://example.com/images/gourmet-bakers.png"
        );

        return solutionCard;
    }

    public ArrayList<SolutionCardDTO> getFeaturedSolutions() {
        SolutionCardDTO solutionCard1 = new SolutionCardDTO(
            1L,
            SolutionType.PRODUCT,
            "FEATURED - Luxury Wedding Cake",
            "Catering",
            "A handcrafted 3-tier wedding cake with customizable flavors.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(List.of("Wedding", "Birthday")),
            150.00,
            10,
            "https://example.com/images/wedding-cake.png",
            true,
            20L,
            "Gourmet Bakers",
            "https://example.com/images/gourmet-bakers.png"
        );

        SolutionCardDTO solutionCard2 = new SolutionCardDTO(
            2L,
            SolutionType.SERVICE,
            "FEATURED - DJ Services",
            "Entertainment",
            null, // descripiton - only for Product
            2,
            6,
            new ArrayList<>(List.of("Wedding", "Corporate")),
            500.00,
            15,
            "https://example.com/images/dj-service.png",
            true,
            25L,
            "Party Vibes Inc.",
            "https://example.com/images/party-vibes.png"
        );

        ArrayList<SolutionCardDTO> featuredSolutions = new ArrayList<>();
        featuredSolutions.add(solutionCard1);
        featuredSolutions.add(solutionCard2);

        return featuredSolutions;
    }

    public ArrayList<SolutionCardDTO> filterSolutions(String search, String type, String category, ArrayList<String> eventTypes, String company, double minPrice, double maxPrice, LocalDate startDate, LocalDate endDate, Boolean isAvailable, Pageable pageable) {
        SolutionCardDTO solutionCard1 = new SolutionCardDTO(
            1L,
            SolutionType.PRODUCT,
            "FILTERED - Luxury Wedding Cake",
            "Catering",
            "A handcrafted 3-tier wedding cake with customizable flavors.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(List.of("Wedding", "Birthday")),
            150.00,
            10,
            "https://example.com/images/wedding-cake.png",
            true,
            20L,
            "Gourmet Bakers",
            "https://example.com/images/gourmet-bakers.png"
        );

        SolutionCardDTO solutionCard2 = new SolutionCardDTO(
            2L,
            SolutionType.SERVICE,
            "FILTERED - DJ Services",
            "Entertainment",
            null, // descripiton - only for Product
            2,
            6,
            new ArrayList<>(List.of("Wedding", "Corporate")),
            500.00,
            15,
            "https://example.com/images/dj-service.png",
            true,
            25L,
            "Party Vibes Inc.",
            "https://example.com/images/party-vibes.png"
        );

        ArrayList<SolutionCardDTO> filteredSolutions = new ArrayList<>();
        filteredSolutions.add(solutionCard1);
        filteredSolutions.add(solutionCard2);

        return filteredSolutions;
        //return solutionRepository.findFilteredSolutions(search, type, category, eventTypes, company, minPrice, maxPrice, startDate, endDate, isAvailable, pageable);
    }
}
