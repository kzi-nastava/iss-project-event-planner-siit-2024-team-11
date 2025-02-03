package org.example.eventy.solutions.dtos;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO extends SolutionDTO {
    private Long id;

    public ProductDTO() {
        super();
    }

    public ProductDTO(Long id, String name, String description, double price, double discount, List<EventTypeDTO> relatedEventTypes, List<String> images, boolean visibility, boolean availability) {
        super(name, description, price, discount, relatedEventTypes, images, visibility, availability);
        this.id = id;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        super.setName(product.getName());
        super.setDescription(product.getDescription());
        super.setPrice(product.getPrice());
        super.setDiscount(product.getDiscount());
        super.setRelatedEventTypes(product.getEventTypes().stream().map(EventTypeDTO::new).collect(Collectors.toList()));
        super.setImages(product.getImageUrls().stream().map(PicturePath::getPath).map(PictureService::getImage).collect(Collectors.toList()));
        super.setAvailability(product.isAvailable());
        super.setVisibility(product.isVisible());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
