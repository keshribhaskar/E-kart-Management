package com.assignment.ekart.ekartms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartProduct {

    private Integer cartProductId;
    @Valid
    @JsonProperty("product")
    private Product product;
    @PositiveOrZero(message = "{product.invalid.quantity}")
    @JsonProperty("quantity")
    private Integer quantity;
}
