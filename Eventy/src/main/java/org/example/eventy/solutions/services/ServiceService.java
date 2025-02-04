package org.example.eventy.solutions.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.services.EventService;
import org.example.eventy.events.services.EventTypeService;
import org.example.eventy.solutions.dtos.services.*;
import org.example.eventy.solutions.models.Category;
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

    private Collection<org.example.eventy.solutions.models.Service> allServices = new ArrayList<>();

    public Service createService(CreateServiceDTO createServiceDTO) {
        Service service = new Service();
        service.setName(createServiceDTO.getName());
        service.setDescription(createServiceDTO.getDescription());
        service.setPrice(createServiceDTO.getPrice());
        service.setDiscount((int) createServiceDTO.getDiscount());
        service.setImageUrls(null); // TO-DO
        service.setProvider((SolutionProvider) userService.get(createServiceDTO.getProviderId()));
        service.setCategory(solutionCategoryService.getCategory(createServiceDTO.getCategoryId()));
        List<EventType> eventTypes = new ArrayList<>();
        for (Long eid: createServiceDTO.getRelatedEventTypeIds()) {
            eventTypes.add(eventTypeService.get(eid));
        }
        service.setEventTypes(eventTypes);
        service.setSpecifics(createServiceDTO.getSpecifics());
        service.setMinReservationTime(createServiceDTO.getMinReservationTime());
        service.setMaxReservationTime(createServiceDTO.getMaxReservationTime());
        service.setReservationDeadline(createServiceDTO.getReservationDeadline());
        service.setCancellationDeadline(createServiceDTO.getCancellationDeadline());
        service.setReservationType(createServiceDTO.isAutomaticReservationAcceptance() ? ReservationConfirmationType.AUTOMATIC : ReservationConfirmationType.MANUAL);
        return solutionRepository.save(service);
    }

    /*public Collection<GetServiceDTO> getServices(String name, CategoryDTO category, EventTypeDTO eventType, double minPrice, double maxPrice, boolean available) {
        Collection<GetServiceDTO> getServiceDTOs = new ArrayList<>();
        allServices.forEach(s -> getServiceDTOs.add(new GetServiceDTO(s)));
        return getServiceDTOs;
    }*/

    /*
    public Optional<GetServiceDTO> getService(long id) {
        return Optional.of(new GetServiceDTO(allServices.stream().filter(s -> s.getId() == id).findFirst().get()));
    }*/

    public Optional<UpdatedServiceDTO> updateService(UpdateServiceDTO updateServiceDTO) {
        Optional<org.example.eventy.solutions.models.Service> oldService = allServices.stream().filter(s -> s.getId() == updateServiceDTO.getId()).findFirst();
        if (!oldService.isPresent()) {
            return Optional.empty();
        }
        UpdatedServiceDTO updatedServiceDTO = new UpdatedServiceDTO();
        updatedServiceDTO.setId(updateServiceDTO.getId());
        updatedServiceDTO.setName(updateServiceDTO.getName());
        updatedServiceDTO.setDescription(updateServiceDTO.getDescription());
        updatedServiceDTO.setPrice(updateServiceDTO.getPrice());
        updatedServiceDTO.setDiscount(updateServiceDTO.getDiscount());
        updatedServiceDTO.setImageUrls(updateServiceDTO.getImageUrls());
        updatedServiceDTO.setVisible(updateServiceDTO.isVisible());
        updatedServiceDTO.setAvailable(updateServiceDTO.isAvailable());
        updatedServiceDTO.setCategory(updateServiceDTO.getCategory());
        updatedServiceDTO.setRelatedEventTypes(updateServiceDTO.getRelatedEventTypes());
        updatedServiceDTO.setSpecifics(updateServiceDTO.getSpecifics());
        updatedServiceDTO.setMinReservationTime(updateServiceDTO.getMinReservationTime());
        updatedServiceDTO.setMaxReservationTime(updateServiceDTO.getMaxReservationTime());
        updatedServiceDTO.setReservationDeadline(updateServiceDTO.getReservationDeadline());
        updatedServiceDTO.setCancellationDeadline(updateServiceDTO.getCancellationDeadline());
        updatedServiceDTO.setAutomaticReservationAcceptance(updateServiceDTO.getAutomaticReservationAcceptance());
        return Optional.of(updatedServiceDTO);
    }

    public void deleteService(long id) {
        allServices.removeIf(s -> s.getId() == id);
    }

    public Solution getService(Long serviceId) {
        return solutionRepository.findById(serviceId).orElse(null);
    }
}
