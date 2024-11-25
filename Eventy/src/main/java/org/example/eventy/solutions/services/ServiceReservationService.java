package org.example.eventy.solutions.services;

import org.example.eventy.solutions.dtos.ServiceReservationDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;

@Service
public class ServiceReservationService {
    // @Autowired
    // private ServiceReservationRepository serviceReservationRepository;

    public ServiceReservationDTO createServiceReservation(ServiceReservationDTO serviceReservation) {
        ServiceReservationDTO newServiceReservation = new ServiceReservationDTO();

        newServiceReservation.setId(serviceReservation.getId());
        newServiceReservation.setSelectedEventId(serviceReservation.getSelectedEventId());
        newServiceReservation.setSelectedServiceId(serviceReservation.getSelectedServiceId());
        newServiceReservation.setReservationStartDateTime(serviceReservation.getReservationStartDateTime());
        newServiceReservation.setReservationEndDateTime(serviceReservation.getReservationEndDateTime());

        return saveServiceReservation(newServiceReservation);
    }

    public ServiceReservationDTO getServiceReservation(Long serviceReservationId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2024, Calendar.JUNE, 15, 14, 0);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2024, Calendar.JUNE, 15, 17, 0);

        ServiceReservationDTO serviceReservation = new ServiceReservationDTO(
                serviceReservationId, 3L, 2L, startDateTime, endDateTime
        );

        return serviceReservation;
    }

    public ArrayList<ServiceReservationDTO> getReservationsByServiceId(Long serviceId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2024, Calendar.JUNE, 15, 14, 30);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2024, Calendar.JUNE, 15, 17, 15);

        ServiceReservationDTO serviceReservation1 = new ServiceReservationDTO(
                1L, 2L, serviceId, startDateTime, endDateTime
        );

        startDateTime.set(2025, Calendar.JANUARY, 10, 8, 0);
        endDateTime.set(2025, Calendar.JANUARY, 10, 11, 30);

        ServiceReservationDTO serviceReservation2 = new ServiceReservationDTO(
                2L, 3L, serviceId, startDateTime, endDateTime
        );

        ArrayList<ServiceReservationDTO> serviceReservations = new ArrayList<>();
        serviceReservations.add(serviceReservation1);
        serviceReservations.add(serviceReservation2);

        return serviceReservations;
    }

    public ArrayList<ServiceReservationDTO> getReservationsByEventId(Long eventId) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(2026, Calendar.DECEMBER, 24, 19, 0);
        Calendar endDateTime = Calendar.getInstance();
        endDateTime.set(2026, Calendar.DECEMBER, 24, 20, 30);

        ServiceReservationDTO serviceReservation1 = new ServiceReservationDTO(
                1L, eventId, 2L, startDateTime, endDateTime
        );

        startDateTime.set(2027, Calendar.MARCH, 1, 9, 0);
        endDateTime.set(2027, Calendar.MARCH, 1, 12, 0);

        ServiceReservationDTO serviceReservation2 = new ServiceReservationDTO(
                2L, eventId, 3L, startDateTime, endDateTime
        );

        ArrayList<ServiceReservationDTO> serviceReservations = new ArrayList<>();
        serviceReservations.add(serviceReservation1);
        serviceReservations.add(serviceReservation2);

        return serviceReservations;
    }

    public ServiceReservationDTO saveServiceReservation(ServiceReservationDTO serviceReservations) {
        return serviceReservations;
    }
}
