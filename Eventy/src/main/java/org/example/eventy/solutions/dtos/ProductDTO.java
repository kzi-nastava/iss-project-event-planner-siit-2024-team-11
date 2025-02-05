package org.example.eventy.solutions.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private List<EventTypeDTO> relatedEventTypes;
    private List<String> images;
    @JsonProperty("isVisible")
    private boolean isVisible;
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    public ProductDTO() {
        super();
    }

    public ProductDTO(Long id, String name, String description, double price, double discount, List<EventTypeDTO> relatedEventTypes, List<String> images, boolean isVisible, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.relatedEventTypes = relatedEventTypes;
        this.images = images;
        this.isVisible = isVisible;
        this.isAvailable = isAvailable;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.setName(product.getName());
        this.setDescription(product.getDescription());
        this.setPrice(product.getPrice());
        this.setDiscount(product.getDiscount());
        this.setRelatedEventTypes(product.getEventTypes().stream().map(EventTypeDTO::new).collect(Collectors.toList()));
        this.setImages(product.getImageUrls().stream().map(PicturePath::getPath).map(PictureService::getImage).collect(Collectors.toList()));
        this.setIsAvailable(product.isAvailable());
        this.setIsVisible(product.isVisible());
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
