package org.example.eventy.solutions.dtos;

import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.common.models.SolutionType;
import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;

import java.util.ArrayList;

public class SolutionCardDTO {
    private Long solutionId; // serviceId or productId
    private SolutionType type; // "SERVICE" or "PRODUCT"
    private String name;
    private String categoryName;
    private String description; // only for products
    private String specifics; // only for services
    private Integer minReservationTime; // only for services
    private Integer maxReservationTime; // only for services
    private Integer reservationDeadline; // only for services
    private Integer cancellationDeadline; // only for services
    private ReservationConfirmationType reservationType; // only for services
    private ArrayList<String> eventTypeNames;
    private Double price;
    private Integer discount;
    private String firstImageUrl;
    private Boolean isAvailable;
    private Long providerId;
    private String providerName;
    private String providerImageUrl;

    public SolutionCardDTO() {

    }

    public SolutionCardDTO(Long solutionId, SolutionType type, String name, String categoryName, String description, String specifics, Integer minReservationTime, Integer maxReservationTime, Integer reservationDeadline, Integer cancellationDeadline, ReservationConfirmationType reservationType, ArrayList<String> eventTypeNames, Double price, Integer discount, String firstImageUrl, Boolean isAvailable, Long providerId, String providerName, String providerImageUrl) {
        this.solutionId = solutionId;
        this.type = type;
        this.name = name;
        this.categoryName = categoryName;
        this.description = description;
        this.specifics = specifics;
        this.minReservationTime = minReservationTime;
        this.maxReservationTime = maxReservationTime;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationType = reservationType;
        this.eventTypeNames = eventTypeNames;
        this.price = price;
        this.discount = discount;
        this.firstImageUrl = firstImageUrl;
        this.isAvailable = isAvailable;
        this.providerId = providerId;
        this.providerName = providerName;
        this.providerImageUrl = providerImageUrl;
    }

    public SolutionCardDTO(Solution solution) {
        this.solutionId = solution.getId();
        this.type = solution.getDescription() != null ? SolutionType.PRODUCT : SolutionType.SERVICE;
        this.name = solution.getName();
        this.categoryName = solution.getCategory().getName();
        if (solution instanceof Service) {
            this.specifics = ((Service) solution).getSpecifics();
            this.minReservationTime = ((Service) solution).getMinReservationTime();
            this.maxReservationTime = ((Service) solution).getMaxReservationTime();
            this.reservationDeadline = ((Service) solution).getReservationDeadline();
            this.cancellationDeadline = ((Service) solution).getCancellationDeadline();
            this.reservationType = ((Service) solution).getReservationType();
            this.description = null;
        } else {
            this.specifics = null;
            this.minReservationTime = null;
            this.maxReservationTime = null;
            this.reservationDeadline = null;
            this.cancellationDeadline = null;
            this.reservationType = null;
            this.description = ((Product) solution).getDescription();
        }
        this.eventTypeNames = new ArrayList<String>();
        for (EventType eventType : solution.getEventTypes()) {
            this.eventTypeNames.add(eventType.getName());
        }
        this.price = solution.getPrice();
        this.discount = solution.getDiscount();
        this.firstImageUrl = solution.getImageUrls().get(0).getPath();
        this.isAvailable = solution.isAvailable();
        this.providerId = solution.getProvider().getId();
        this.providerName = solution.getProvider().getName();
        this.providerImageUrl = solution.getImageUrls().get(0).getPath();
    }

    public Long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Long solutionId) {
        this.solutionId = solutionId;
    }

    public SolutionType getType() {
        return type;
    }

    public void setType(SolutionType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ReservationConfirmationType getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationConfirmationType reservationType) {
        this.reservationType = reservationType;
    }

    public ArrayList<String> getEventTypeNames() {
        return eventTypeNames;
    }

    public void setEventTypeNames(ArrayList<String> eventTypeNames) {
        this.eventTypeNames = eventTypeNames;
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

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderImageUrl() {
        return providerImageUrl;
    }

    public void setProviderImageUrl(String providerImageUrl) {
        this.providerImageUrl = providerImageUrl;
    }
}
