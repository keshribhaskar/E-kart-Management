package com.assignment.ekart.ekartms.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "CART_PRODUCT")
@Data
@Builder
public class CartProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartProductId;
    private Integer productId;
    private Integer quantity;
}
