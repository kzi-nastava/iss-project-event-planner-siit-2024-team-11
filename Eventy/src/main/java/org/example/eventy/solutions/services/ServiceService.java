package org.example.eventy.solutions.services;

import org.example.eventy.common.models.SolutionType;
import org.example.eventy.solutions.dtos.ServiceReservationDTO;
import org.example.eventy.solutions.dtos.SolutionCardDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceService {
    // @Autowired
    // private ServiceRepository serviceRepository;

    public ArrayList<SolutionCardDTO> getServices(Pageable pageable) {
        SolutionCardDTO serviceCard1 = new SolutionCardDTO(
            1L,
            SolutionType.SERVICE,
            "Wedding Catering",
            "Catering",
            null, // description - only for Product
            3,
            8,
            new ArrayList<>(List.of("Wedding", "Reception")),
            50.00,
            5,
            "https://example.com/images/wedding_catering.jpg",
            true,
            601L,
            "Gourmet Delights",
            "https://example.com/images/gourmet_delights.jpg"
        );

        SolutionCardDTO serviceCard2 = new SolutionCardDTO(
            2L,
            SolutionType.SERVICE,
            "Live Music Band",
            "Entertainment",
            null, // description - only for Product
            2,
            5,
            new ArrayList<>(List.of("Wedding", "Party")),
            1200.00,
            0,
            "https://example.com/images/music_band.jpg",
            false,
            602L,
            "Melody Makers",
            "https://example.com/images/melody_makers.jpg"
        );

        ArrayList<SolutionCardDTO> services = new ArrayList<>();
        services.add(serviceCard1);
        services.add(serviceCard2);

        return services;
    }

    public SolutionCardDTO getServiceCard(Long serviceId) {
        SolutionCardDTO serviceCard = new SolutionCardDTO(
            serviceId,
            SolutionType.SERVICE,
            "Wedding Catering",
            "Catering",
            null, // description - only for Product
            3,
            8,
            new ArrayList<>(List.of("Wedding", "Reception")),
            50.00,
            5,
            "https://example.com/images/wedding_catering.jpg",
            true,
            601L,
            "Gourmet Delights",
            "https://example.com/images/gourmet_delights.jpg"
        );

        return serviceCard;
    }
}
