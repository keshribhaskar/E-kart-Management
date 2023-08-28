package com.assignment.ekart.ekartms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CartProduct {
    @JsonProperty("cart_Product_Id")
    private Integer cartProductId;
    @Valid
    @JsonProperty("product")
    private Product product;
    @PositiveOrZero(message = "{product.invalid.quantity}")
    @JsonProperty("quantity")
    private Integer quantity;
}
