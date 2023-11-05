package com.assignment.ekart.ekartms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCart {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cartId;
    @NotNull(message = "{email.absent}")
    @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+" , message = "{invalid.email.format}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerEmailId;
    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CartProduct> cartProducts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
