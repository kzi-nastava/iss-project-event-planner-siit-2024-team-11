package org.example.eventy.solutions.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.dtos.services.*;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.SolutionProvider;
import org.springframework.data.domain.Pageable;

import java.util.*;

@org.springframework.stereotype.Service
public class ServiceService {
    // @Autowired
    // private ServiceRepository serviceRepository;

    private Collection<org.example.eventy.solutions.models.Service> allServices = new ArrayList<>();

    private void setTestData() {
        Category category1 = new Category(1L, "Category 1", "Description for Category 1", Status.ACCEPTED);
        Category category2 = new Category(2L, "Category 2", "Description for Category 2", Status.ACCEPTED);

        Set<Category> recommendedCategoriesForEventType1 = new HashSet<>();
        recommendedCategoriesForEventType1.add(category1);

        Set<Category> recommendedCategoriesForEventType2 = new HashSet<>();
        recommendedCategoriesForEventType2.add(category2);

        EventType eventType1 = new EventType(1L, "Event Type 1", "Description for Event Type 1", true, recommendedCategoriesForEventType1);
        EventType eventType2 = new EventType(2L, "Event Type 2", "Description for Event Type 2", true, recommendedCategoriesForEventType2);

        List<EventType> eventTypeList1 = new ArrayList<>();
        eventTypeList1.add(eventType1);  // List with first EventType

        List<EventType> eventTypeList2 = new ArrayList<>();
        eventTypeList2.add(eventType2);

        org.example.eventy.solutions.models.Service service1 = new org.example.eventy.solutions.models.Service(
                1L, "Service 1", "Description for Service 1", 100.0, 10, new ArrayList<>(Arrays.asList(new PicturePath(4L, "https://example.com/images/wedding_catering.jpg"))),
                true, true, false, category1, eventTypeList1,
                null, "Specifics for Service 1", 10, 120, 30, 15, ReservationConfirmationType.AUTOMATIC, null
        );

        org.example.eventy.solutions.models.Service service2 = new org.example.eventy.solutions.models.Service(
                2L, "Service 2", "Description for Service 2", 200.0, 20, new ArrayList<>(Arrays.asList(new PicturePath(6L, "https://example.com/images/wedding_catering.jpg"))),
                true, true, false, category2, eventTypeList2,
                null, "Specifics for Service 2", 15, 90, 45, 20, ReservationConfirmationType.MANUAL, null
        );

        org.example.eventy.solutions.models.Service service3 = new org.example.eventy.solutions.models.Service(
                3L, "Service 3", "Description for Service 3", 150.0, 15, new ArrayList<>(Arrays.asList(new PicturePath(7L, "https://example.com/images/wedding_catering.jpg"))),
                true, true, false, category1, eventTypeList2,
                null, "Specifics for Service 3", 20, 100, 60, 30, ReservationConfirmationType.AUTOMATIC, null
        );

        org.example.eventy.solutions.models.Service service4 = new org.example.eventy.solutions.models.Service(
                4L, "Service 4", "Description for Service 4", 250.0, 25, new ArrayList<>(Arrays.asList(new PicturePath(8L, "https://example.com/images/wedding_catering.jpg"))),
                true, true, false, category2, eventTypeList1,
                null, "Specifics for Service 4", 30, 150, 90, 45, ReservationConfirmationType.MANUAL, null
        );

        org.example.eventy.solutions.models.Service service5 = new org.example.eventy.solutions.models.Service(
                5L, "Service 5", "Description for Service 5", 180.0, 18, new ArrayList<>(Arrays.asList(new PicturePath(9L, "https://example.com/images/wedding_catering.jpg"))),
                true, true, false, category1, eventTypeList2,
                null, "Specifics for Service 5", 25, 110, 75, 35, ReservationConfirmationType.AUTOMATIC, null
        );

        allServices.add(service1);
        allServices.add(service2);
        allServices.add(service3);
        allServices.add(service4);
        allServices.add(service5);
    }

    public ServiceService() {
        setTestData();
    }

    public CreatedServiceDTO createService(CreateServiceDTO createServiceDTO) {
        CreatedServiceDTO createdServiceDTO = new CreatedServiceDTO();
        createdServiceDTO.setId(1337L);
        createdServiceDTO.setName(createServiceDTO.getName());
        createdServiceDTO.setDescription(createServiceDTO.getDescription());
        createdServiceDTO.setPrice(createServiceDTO.getPrice());
        createdServiceDTO.setDiscount(createServiceDTO.getDiscount());
        createdServiceDTO.setImageUrls(createServiceDTO.getImageUrls());
        createdServiceDTO.setCategory(createServiceDTO.getCategory());
        createdServiceDTO.setRelatedEventTypes(createServiceDTO.getRelatedEventTypes());
        createdServiceDTO.setSpecifics(createServiceDTO.getSpecifics());
        createdServiceDTO.setMinReservationTime(createServiceDTO.getMinReservationTime());
        createdServiceDTO.setMaxReservationTime(createServiceDTO.getMaxReservationTime());
        createdServiceDTO.setReservationDeadline(createServiceDTO.getReservationDeadline());
        createdServiceDTO.setCancellationDeadline(createServiceDTO.getCancellationDeadline());
        createdServiceDTO.setAutomaticReservationAcceptance(createServiceDTO.getAutomaticReservationAcceptance());
        return createdServiceDTO;
    }

    /*public Collection<GetServiceDTO> getServices(String name, CategoryDTO category, EventTypeDTO eventType, double minPrice, double maxPrice, boolean available) {
        Collection<GetServiceDTO> getServiceDTOs = new ArrayList<>();
        allServices.forEach(s -> getServiceDTOs.add(new GetServiceDTO(s)));
        return getServiceDTOs;
    }*/

    public Optional<GetServiceDTO> getService(long id) {
        return Optional.of(new GetServiceDTO(allServices.stream().filter(s -> s.getId() == id).findFirst().get()));
    }

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

    public ArrayList<Solution> getServices(Pageable pageable) {
        ArrayList<Solution> services = generateServiceExamples();
        return services;
    }

    public Solution getService(Long serviceId) {
        ArrayList<Solution> services = generateServiceExamples();
        Solution service = services.get(0);
        service.setId(serviceId);

        return service;
    }

    public ArrayList<Solution> generateServiceExamples() {
        Category category1 = new Category();
        category1.setName("Catering");
        ArrayList<EventType> eventTypes = new ArrayList<EventType>();
        EventType eventType1 = new EventType();
        eventType1.setName("Wedding");
        EventType eventType2 = new EventType();
        eventType2.setName("Birthday");
        eventTypes.add(eventType1);
        eventTypes.add(eventType2);
        ArrayList<PicturePath> imageUrls = new ArrayList<PicturePath>();
        imageUrls.add(new PicturePath(9L, "https://example.com/solution.png"));
        SolutionProvider provider = new SolutionProvider();
        provider.setName("TacTac");
        provider.setEmail("cakes.luxury@gmail.com");
        provider.setImageUrls(imageUrls);

        Service service1 = new Service();
        service1.setId(1L);
        service1.setName("Catering Food");
        service1.setCategory(category1);
        service1.setMinReservationTime(1);
        service1.setMaxReservationTime(3);
        service1.setDescription(null);
        service1.setEventTypes(eventTypes);
        service1.setPrice(2220.00);
        service1.setDiscount(15);
        service1.setImageUrls(imageUrls);
        service1.setAvailable(true);
        service1.setProvider(provider);

        Category category2 = new Category();
        category2.setName("Entertainment");
        provider.setEmail("exit.festival@gmail.com");

        Service service2 = new org.example.eventy.solutions.models.Service();
        service2.setId(2L);
        service2.setName("DJ Services");
        service2.setCategory(category2);
        service2.setMinReservationTime(2);
        service2.setMaxReservationTime(6);
        service2.setDescription(null);
        service2.setEventTypes(eventTypes);
        service2.setPrice(150.00);
        service2.setDiscount(10);
        service2.setImageUrls(imageUrls);
        service2.setAvailable(true);
        service2.setProvider(provider);

        ArrayList<Solution> services = new ArrayList<Solution>();
        services.add(service1);
        services.add(service2);

        return services;
    }
}
