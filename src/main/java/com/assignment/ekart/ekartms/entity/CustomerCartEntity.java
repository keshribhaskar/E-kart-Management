package com.assignment.ekart.ekartms.entity;

import com.assignment.ekart.ekartms.model.CartProduct;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "CustomerCart")
public class CustomerCartEntity {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer cartId;

    @Column(name = "customer_email_id")
    private String customerEmailId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name ="cart_products")
    private Set<CartProductEntity> cartProducts;
}
