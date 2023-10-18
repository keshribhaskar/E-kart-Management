package com.assignment.ekart.ekartms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    private Integer cartProductId;
    @Valid
    @JsonProperty("product")
    private Product product;
    @PositiveOrZero(message = "{product.invalid.quantity}")
    @JsonProperty("quantity")
    private Integer quantity;
}
