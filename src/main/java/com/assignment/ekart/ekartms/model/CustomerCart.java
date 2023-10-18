package com.assignment.ekart.ekartms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
@Data
@Builder
public class CustomerCart {

    private Integer cartId;
    @NotNull(message = "{email.absent}")
    @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+" , message = "{invalid.email.format}")
    private String customerEmailId;
    @Valid
    private Set<CartProduct> cartProducts;
}
