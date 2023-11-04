package com.assignment.ekart.ekartms.controller;

import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.model.Product;
import com.assignment.ekart.ekartms.service.CustomerCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@RestController
@RequestMapping(value = "/kartApi")
@Validated
@Slf4j
public class EkartContorller {

    @Autowired
    private CustomerCartService customerCartService;

    @PostMapping(value = "/products")
    public ResponseEntity<String> addProductToCart(@Valid @RequestBody CustomerCart customerCart)
            throws Exception {
        log.info("Received a request to add products for " + customerCart.getCustomerEmailId());
        String message = customerCartService.addProductToCart(customerCart);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/customer/{customerEmailId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws Exception {

        customerCartService.deleteProductFromCart(customerEmailId);
        String message = "Product deleted from cart success";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{customerEmailId}/products")
    public ResponseEntity<CustomerCart> getProductsFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws Exception {
        log.info("Received a request to get products details from the cart of "+customerEmailId);

        CustomerCart cartProducts = customerCartService.getProductsFromCart(customerEmailId);
        return new ResponseEntity<>(cartProducts, HttpStatus.OK);
    }
}
