package org.example.eventy.solutions.services;

import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.common.models.Status;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.solutions.dtos.services.*;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private SolutionCategoryService solutionCategoryService;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PictureService pictureService;

    public Service createService(CreateServiceDTO createServiceDTO) {
        Service service = new Service();
        service.setName(createServiceDTO.getName());
        service.setDescription(createServiceDTO.getDescription());
        service.setPrice(createServiceDTO.getPrice());
        service.setDiscount((int) createServiceDTO.getDiscount());
        service.setImageUrls(pictureService.save(createServiceDTO.getImageUrls()));
        service.setProvider((SolutionProvider) userService.get(createServiceDTO.getProviderId()));
        service.setCategory(solutionCategoryService.getCategory(createServiceDTO.getCategoryId()));
        List<EventType> eventTypes = new ArrayList<>();
        for (Long eid: createServiceDTO.getRelatedEventTypeIds()) {
            eventTypes.add(eventTypeService.get(eid));
        }
        service.setAvailable(true);
        service.setVisible(service.getCategory().getStatus() == Status.ACCEPTED ? true : false);
        service.setEventTypes(eventTypes);
        service.setSpecifics(createServiceDTO.getSpecifics());
        service.setMinReservationTime(createServiceDTO.getMinReservationTime());
        service.setMaxReservationTime(createServiceDTO.getMaxReservationTime());
        service.setReservationDeadline(createServiceDTO.getReservationDeadline());
        service.setCancellationDeadline(createServiceDTO.getCancellationDeadline());
        service.setReservationType(createServiceDTO.isAutomaticReservationAcceptance() ? ReservationConfirmationType.AUTOMATIC : ReservationConfirmationType.MANUAL);
        return solutionRepository.save(service);
    }

    public Service updateService(UpdateServiceDTO updateServiceDTO) {
        Service service;
        try {
            service = (Service) solutionRepository.findById(updateServiceDTO.getId()).orElse(null);
        } catch (Exception e) {
            return null;
        }

        if (service == null) {
            return null;
        }

        service.setName(updateServiceDTO.getName());
        service.setDescription(updateServiceDTO.getDescription());
        service.setPrice(updateServiceDTO.getPrice());
        service.setDiscount(updateServiceDTO.getDiscount());
        service.setImageUrls(pictureService.save(updateServiceDTO.getImageUrls())); // TO-DO
        service.setVisible(updateServiceDTO.isVisible());
        service.setAvailable(updateServiceDTO.isAvailable());
        List<EventType> eventTypes = new ArrayList<>();
        for (Long eid: updateServiceDTO.getRelatedEventTypeIds()) {
            eventTypes.add(eventTypeService.get(eid));
        }
        service.setEventTypes(eventTypes);
        service.setSpecifics(updateServiceDTO.getSpecifics());
        service.setMinReservationTime(updateServiceDTO.getMinReservationTime());
        service.setMaxReservationTime(updateServiceDTO.getMaxReservationTime());
        service.setReservationDeadline(updateServiceDTO.getReservationDeadline());
        service.setCancellationDeadline(updateServiceDTO.getCancellationDeadline());
        service.setReservationType(updateServiceDTO.getAutomaticReservationAcceptance() ? ReservationConfirmationType.AUTOMATIC : ReservationConfirmationType.MANUAL);
        return solutionRepository.save(service);
    }
    
    public Solution getService(Long serviceId) {
        return solutionRepository.findById(serviceId).orElse(null);
    }
}
