package org.example.eventy.solutions.dtos;

public class ProductPurchaseDTO {
    private Long productId;
    private Long organizerId;

    public ProductPurchaseDTO() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }
}
