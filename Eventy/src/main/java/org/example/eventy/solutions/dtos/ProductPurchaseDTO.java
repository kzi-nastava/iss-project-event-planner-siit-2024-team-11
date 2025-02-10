package org.example.eventy.solutions.dtos;

public class ProductPurchaseDTO {
    private Long productId;
    private Long eventId;

    public ProductPurchaseDTO() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
