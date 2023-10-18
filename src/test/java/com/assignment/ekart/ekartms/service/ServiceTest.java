package com.assignment.ekart.ekartms.service;

import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerCartService customerCartService;
    @Test
    public void addProductToCartTest() throws Exception {

        Product p = Product.builder()
                .productId(1)
                .name("Shampoo")
                .brand("dove")
                .price(1.5)
                .availableQuantity(15)
                .build();

        CartProduct cp = CartProduct.builder()
                .cartProductId(2)
                .quantity(2)
                .product(p)
                .build();

        Set<CartProduct> cps = new HashSet<>();
        cps.add(cp);
        CustomerCart cc = CustomerCart.builder()
                .cartId(1)
                .customerEmailId("abc@gmail.com")
                .cartProducts(cps)
                .build();

        int expected = 1;
        int actual = customerCartService.addProductToCart(cc);
        Assertions.assertEquals(expected,actual);
    }

}
