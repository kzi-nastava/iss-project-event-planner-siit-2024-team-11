package org.example.eventy.solutions.dtos;

import org.example.eventy.events.dtos.EventTypeDTO;

import java.util.List;

public class ProductDTO extends SolutionDTO {
    private Long id;

    public ProductDTO() {
        super();
    }

    public ProductDTO(Long id, String name, String description, double price, double discount, List<EventTypeDTO> relatedEventTypes, List<String> images, boolean visibility, boolean availability) {
        super(name, description, price, discount, relatedEventTypes, images, visibility, availability);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
