package com.assignment.ekart.ekartms.service;

import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static com.assignment.ekart.ekartms.config.Constant.CART_DELETE_SUCCESS;
import static com.assignment.ekart.ekartms.config.Constant.CART_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerCartService customerCartService;
    @Autowired
    private ObjectMapper mapper;

    Set<CartProduct> cps = new HashSet<>();
    @BeforeEach
    public void setUp(){
//        Set<CartProduct> cps = new HashSet<>();
        Product p = Product.builder()
                .productId(1)
                .name("Shampoo")
                .description("hair shampoo")
                .category("shampoo")
                .brand("dove")
                .price(3.0)
                .availableQuantity(850)
                .build();

        CartProduct cp = CartProduct.builder()
                .quantity(5)
                .product(p)
                .build();

        cps.add(cp);

    }
    @Test
    public void addProductToCartTest() throws Exception {
        CustomerCart cc = CustomerCart.builder()
                .cartId(1)
                .customerEmailId("k@gmail.com")
                .cartProducts(cps)
                .build();
        String expected = "Product added to cart successfully.";
        String actual = customerCartService.addProductToCart(cc);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getProductsFromCartTest() throws Exception {
        String expected = "{\"error\":\"I/O error on GET request for \\\"http://localhost:8082/productApi/product/1\\\": Connection refused: connect\"}";
        CustomerCart cc = CustomerCart.builder()
                .cartId(1)
                .customerEmailId("k@gmail.com")
                .cartProducts(cps)
                .build();
        String custEmailId = "k@gmail.com";
        customerCartService.addProductToCart(cc);
        CustomerCart getProductData = customerCartService.getProductsFromCart(custEmailId);
        String actual = mapper.writeValueAsString(getProductData);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void deleteProductFromCartSuccessTest() throws Exception {
        String expected = CART_DELETE_SUCCESS;
        CustomerCart cc = CustomerCart.builder()
                .cartId(1)
                .customerEmailId("k@gmail.com")
                .cartProducts(cps)
                .build();
        customerCartService.addProductToCart(cc);
        String customerEmailId = "k@gmail.com";
        Integer productId = 1;
        String actual = customerCartService.deleteProductFromCart(customerEmailId);
        Assertions.assertEquals(expected,actual);
    }
    @Test
    public void deleteProductFromCartFailedTest() throws Exception {
        String expected = CART_NOT_FOUND;
        String customerEmailId = "v@gmail.com";
        String actual = customerCartService.deleteProductFromCart(customerEmailId);
        Assertions.assertEquals(expected,actual);
    }
}
