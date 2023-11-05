package com.assignment.ekart.ekartms.controller;

import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.service.CustomerCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.assignment.ekart.ekartms.config.Constant.CART_ADD_SUCCESS;
import static com.assignment.ekart.ekartms.config.Constant.CART_DELETE_SUCCESS;

@RestController
@RequestMapping(value = "/kartApi")
@Validated
@Slf4j
public class EkartContorller {

    @Autowired
    private CustomerCartService customerCartService;

    @PostMapping(value = "/add-products")
    public ResponseEntity<String> addProductToCart(@Valid @RequestBody CustomerCart customerCart)
            throws Exception {
        log.info("Received a request to add products for " + customerCart.getCustomerEmailId());
        String message = customerCartService.addProductToCart(customerCart);
        if(message.equalsIgnoreCase(CART_ADD_SUCCESS)){
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/customer/{customerEmailId}/product")
    public ResponseEntity<String> deleteProductFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws Exception {

        String message = customerCartService.deleteProductFromCart(customerEmailId);
        if(message.equalsIgnoreCase(CART_DELETE_SUCCESS)){
            return new ResponseEntity<>(message, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/customer/{customerEmailId}/products")
    public ResponseEntity<CustomerCart> getProductsFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws Exception {
        log.info("Received a request to get products details from the cart of "+customerEmailId);

        CustomerCart cartProducts = customerCartService.getProductsFromCart(customerEmailId);
        if(StringUtils.isEmpty(cartProducts.getError())){
            return new ResponseEntity<>(cartProducts, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(cartProducts, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
