package org.example.eventy.solutions.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.events.dtos.EventTypeCardDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;

import java.util.ArrayList;
import java.util.Collection;

public class CreateProductDTO {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @Min(0)
    private double price;
    @Min(0)
    @Max(100)
    private int discount;
    @NotNull(message = "Image URLs cannot be null")
    private ArrayList<String> imageUrls;
    @NotNull(message = "Category cannot be null")
    private CategoryWithIDDTO category;
    @NotNull
    private Collection<EventTypeCardDTO> relatedEventTypes;
    @JsonProperty("isVisible")
    private boolean isVisible;
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    public CreateProductDTO() {

    }

    public CreateProductDTO(String name, String description, double price, int discount, ArrayList<String> imageUrls, CategoryWithIDDTO category, Collection<EventTypeCardDTO> relatedEventTypes, boolean isVisible, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageUrls = imageUrls;
        this.category = category;
        this.relatedEventTypes = relatedEventTypes;
        this.isVisible = isVisible;
        this.isAvailable = isAvailable;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public CategoryWithIDDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryWithIDDTO category) {
        this.category = category;
    }

    public Collection<EventTypeCardDTO> getRelatedEventTypes() {
        return relatedEventTypes;
    }

    public void setRelatedEventTypes(Collection<EventTypeCardDTO> relatedEventTypes) {
        this.relatedEventTypes = relatedEventTypes;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        this.isAvailable = available;
    }
}
