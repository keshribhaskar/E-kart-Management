package com.assignment.ekart.ekartms.controller;

import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.model.Product;
import com.assignment.ekart.ekartms.service.CustomerCartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class EkartContorllerTest {

    @InjectMocks
    private EkartContorller ekartController;
    @Mock
    private CustomerCartService customerCartService;
    @Mock
    private RestTemplate template;

    @Test
    public void addProductToCartTest() throws Exception {

        Set<CartProduct> cartProducts = new HashSet<>();
        Product product = Product.builder()
                .productId(1)
                .name("Shampoo")
                .description("hair shampoo")
                .category("shampoo")
                .brand("Clinic plus")
                .price(1.5)
                .availableQuantity(320)
                .build();
        CartProduct cartProduct = CartProduct.builder()
                .cartProductId(1)
                .product(product)
                .quantity(5)
                .build();
        cartProducts.add(cartProduct);
        CustomerCart customer = CustomerCart.builder()
                .cartId(1)
                .customerEmailId("k@gmail.com")
                .cartProducts(cartProducts).build();

        ResponseEntity<String> responseEntity = ekartController.addProductToCart(customer);
        HttpStatusCode expected = CREATED;
        HttpStatusCode actual = responseEntity.getStatusCode();
        Assertions.assertEquals(expected,actual);
    }

    /*
    @Test
    public void getProductsFromCartTest() throws Exception {
        String customerEmailId ="k@gmail.com";
        Product product = Product.builder()
                .productId(1)
                .name("Shampoo")
                .description("hair shampoo")
                .category("shampoo")
                .brand("Clinic plus")
                .price(1.5)
                .availableQuantity(320)
                .build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Set<CartProduct> cartProducts = new HashSet<>();
        CartProduct cartProduct = CartProduct.builder()
                .product(product)
                .quantity(5)
                .cartProductId(1)
                .build();
        cartProducts.add(cartProduct);
        when(customerCartService.getProductsFromCart(customerEmailId)).thenReturn(cartProducts);

        ResponseEntity<Set<CartProduct>> responseEntity = ekartController.getProductsFromCart(customerEmailId);
        HttpStatusCode expected = OK;
        HttpStatusCode actual = responseEntity.getStatusCode();
        Assertions.assertEquals(expected,actual);
    }
    */

    @Test
    public void deleteProductFromCartTest() throws Exception {
        String customerEmailId ="k@gmail.com";
        ResponseEntity<String> responseEntity = ekartController.deleteProductFromCart(customerEmailId);
        HttpStatusCode expected = OK;
        HttpStatusCode actual = responseEntity.getStatusCode();
        Assertions.assertEquals(expected,actual);

    }
}
