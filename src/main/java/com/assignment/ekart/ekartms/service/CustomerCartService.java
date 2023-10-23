package com.assignment.ekart.ekartms.service;

import com.assignment.ekart.ekartms.entity.CartProductEntity;
import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;

import java.util.Set;

public interface CustomerCartService {
    Integer addProductToCart(CustomerCart customerCart) throws Exception;
    Set<CartProduct> getProductsFromCart(String customerEmailId) throws Exception;
    void deleteProductFromCart(String customerEmailId) throws Exception;
    Set<CartProduct> getProducts(Set<CartProduct> cartProducts);
}
