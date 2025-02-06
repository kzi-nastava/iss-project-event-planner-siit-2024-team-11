package org.example.eventy.solutions.dtos.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.dtos.CategoryDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.models.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer discount;
    private List<String> imageUrls;
    @JsonProperty("isVisible")
    private Boolean isVisible;
    @JsonProperty("isAvailable")
    private Boolean isAvailable;
    private CategoryDTO category;
    private Collection<EventTypeDTO> relatedEventTypes;
    private String specifics;
    private Integer minReservationTime;
    private Integer maxReservationTime;
    private Integer reservationDeadline;
    private Integer cancellationDeadline;
    @JsonProperty("automaticReservationAcceptance")
    private Boolean automaticReservationAcceptance;

    public ServiceDTO() { super(); }

    public ServiceDTO(Service service) {
        this.id = service.getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.price = service.getPrice();
        this.discount = service.getDiscount();
        this.imageUrls = service.getImageUrls() == null ? null : service.getImageUrls().stream().map(PicturePath::getPath).map(PictureService::getImage).collect(Collectors.toList());
        this.isVisible = service.isVisible();
        this.isAvailable = service.isAvailable();
        this.category = new CategoryDTO(service.getCategory());
        this.relatedEventTypes = new ArrayList<>();
        // this may be too complicated, maybe there is an easier way
        service.getEventTypes().forEach(et -> this.relatedEventTypes.add(new EventTypeDTO(et.getId(), et.getName(), et.getDescription(), et.getRecommendedSolutionCategories().stream().map(CategoryWithIDDTO::new).toList())));
        this.specifics = service.getSpecifics();
        this.minReservationTime = service.getMinReservationTime();
        this.maxReservationTime = service.getMaxReservationTime();
        this.reservationDeadline = service.getReservationDeadline();
        this.cancellationDeadline = service.getCancellationDeadline();
        this.automaticReservationAcceptance = service.getReservationType() == ReservationConfirmationType.AUTOMATIC;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Collection<EventTypeDTO> getRelatedEventTypes() {
        return relatedEventTypes;
    }

    public void setRelatedEventTypes(Collection<EventTypeDTO> relatedEventTypes) {
        this.relatedEventTypes = relatedEventTypes;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    public Integer getMinReservationTime() {
        return minReservationTime;
    }

    public void setMinReservationTime(Integer minReservationTime) {
        this.minReservationTime = minReservationTime;
    }

    public Integer getMaxReservationTime() {
        return maxReservationTime;
    }

    public void setMaxReservationTime(Integer maxReservationTime) {
        this.maxReservationTime = maxReservationTime;
    }

    public Integer getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(Integer reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public Boolean getAutomaticReservationAcceptance() {
        return automaticReservationAcceptance;
    }

    public void setAutomaticReservationAcceptance(Boolean automaticReservationAcceptance) {
        this.automaticReservationAcceptance = automaticReservationAcceptance;
    }
}
