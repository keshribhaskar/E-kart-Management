package com.assignment.ekart.ekartms.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Product {
    @NotNull(message = "{product.id.absent}")
    private Integer productId;
    private String name;
    private String description;
    private String category;
    private String brand;
    private Double price;
    private Integer availableQuantity;
}
