package org.example.eventy.solutions.dtos;

import org.example.eventy.events.dtos.EventTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class SolutionDTO {
    // Category
    private String name;
    private String description;
    private double price;
    private double discount;
    private List<EventTypeDTO> relatedEventTypes;
    private List<String> images;
    private boolean visibility;
    private boolean availability;

    public SolutionDTO() {

    }

    public SolutionDTO(String name, String description, double price, double discount, List<EventTypeDTO> relatedEventTypes, List<String> images, boolean visibility, boolean availability) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.relatedEventTypes = relatedEventTypes;
        this.images = images;
        this.visibility = visibility;
        this.availability = availability;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<EventTypeDTO> getRelatedEventTypes() {
        return relatedEventTypes;
    }

    public void setRelatedEventTypes(List<EventTypeDTO> relatedEventTypes) {
        this.relatedEventTypes = relatedEventTypes;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
