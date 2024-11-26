package org.example.eventy.solutions.services;

import org.example.eventy.common.models.SolutionType;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    // @Autowired
    // private ProductRepository productRepository;

    public ArrayList<SolutionCardDTO> getProducts(Pageable pageable) {
        SolutionCardDTO productsCard1 = new SolutionCardDTO(
            1L,
            SolutionType.PRODUCT,
            "Wedding Centerpiece",
            "Decorations",
            "Elegant floral centerpiece for wedding tables.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(Arrays.asList("Wedding", "Reception")),
            75.00,
            10,
            "https://example.com/images/centerpiece.jpg",
            true,
            501L,
            "Floral Creations",
            "https://example.com/images/floral_creations.jpg"
        );

        SolutionCardDTO productsCard2 = new SolutionCardDTO(
            1L,
            SolutionType.PRODUCT,
            "Photo Booth Package",
            "Entertainment",
            "Complete photo booth setup with unlimited prints.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(Arrays.asList("Corporate", "Wedding")),
            400.00,
            15,
            "https://example.com/images/photo_booth.jpg",
            true,
            502L,
            "EventSnap Co.",
            "https://example.com/images/eventsnap_co.jpg"
        );

        ArrayList<SolutionCardDTO> products = new ArrayList<>();
        products.add(productsCard1);
        products.add(productsCard2);

        return products;
    }

    public SolutionCardDTO getProductCard(Long productId) {
        SolutionCardDTO productCard = new SolutionCardDTO(
            productId,
            SolutionType.PRODUCT,
            "Wedding Centerpiece",
            "Decorations",
            "Elegant floral centerpiece for wedding tables.",
            null, // minReservationTime - only for Service
            null, // maxReservationTime - only for Service
            new ArrayList<>(Arrays.asList("Wedding", "Reception")),
            75.00,
            10,
            "https://example.com/images/centerpiece.jpg",
            true,
            501L,
            "Floral Creations",
            "https://example.com/images/floral_creations.jpg"
        );

        return productCard;
    }
}
