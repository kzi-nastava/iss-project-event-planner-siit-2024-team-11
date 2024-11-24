package org.example.eventy.solutions.dtos;

import org.example.eventy.common.models.SolutionType;
import org.example.eventy.events.models.EventType;

import java.util.ArrayList;

public class SolutionCardDTO {
    private Long solutionId; // serviceId or productId
    private SolutionType type; // "SERVICE" or "PRODUCT"
    private String name;
    private String categoryName;
    private String description; // only for products
    private Integer minReservationTime; // only for services
    private Integer maxReservationTime; // only for services
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

    public SolutionCardDTO(Long solutionId, SolutionType type, String name, String categoryName, String description, Integer minReservationTime, Integer maxReservationTime, ArrayList<String> eventTypeNames, double price, int discount, String firstImageUrl, boolean isAvailable, Long organiserId, String organiserName, String providerImageUrl) {
        this.solutionId = solutionId;
        this.type = type;
        this.name = name;
        this.categoryName = categoryName;
        this.description = description;
        this.minReservationTime = minReservationTime;
        this.maxReservationTime = maxReservationTime;
        this.eventTypeNames = eventTypeNames;
        this.price = price;
        this.discount = discount;
        this.firstImageUrl = firstImageUrl;
        this.isAvailable = isAvailable;
        this.providerId = organiserId;
        this.providerName = organiserName;
        this.providerImageUrl = providerImageUrl;
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

    public Boolean isAvailable() {
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