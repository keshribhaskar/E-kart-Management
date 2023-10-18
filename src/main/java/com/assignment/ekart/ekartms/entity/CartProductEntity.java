package com.assignment.ekart.ekartms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CART_PRODUCT")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartProductId;
    private Integer productId;
    private Integer quantity;
}
