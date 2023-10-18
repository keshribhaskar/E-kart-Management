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
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate template;

    @PostMapping(value = "/products")
    public ResponseEntity<String> addProductToCart(@Valid @RequestBody CustomerCart customerCart)
            throws Exception {
        log.info("Received a request to add products for " + customerCart.getCustomerEmailId());
        Integer cartId = customerCartService.addProductToCart(customerCart);
        String message = environment.getProperty("CustomerCartAPI.PRODUCT_ADDED_TO_CART");
        return new ResponseEntity<>(message + "  " + cartId, HttpStatus.CREATED);
    }

    @GetMapping(value = "/customer/{customerEmailId}/products")
    public ResponseEntity<Set<CartProduct>> getProductsFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws Exception {
        log.info("Received a request to get products details from the cart of "+customerEmailId);

        Set<CartProduct> cartProducts = customerCartService.getProductsFromCart(customerEmailId);
        for (CartProduct cartProduct : cartProducts) {
            Product product = template.getForEntity("http://localhost:8082" + "/productApi/product/"
                    + cartProduct.getProduct().getProductId(), Product.class).getBody();

            cartProduct.setProduct(product);
        }
        return new ResponseEntity<>(cartProducts, HttpStatus.OK);

    }

    @DeleteMapping(value = "/customer/{customerEmailId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId,
            @NotNull(message = "{product.id.absent}") @PathVariable("productId") Integer productId)
            throws Exception {

        customerCartService.deleteProductFromCart(customerEmailId, productId);
        String message = environment.getProperty("CustomerCartAPI.PRODUCT_DELETED_FROM_CART_SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @PutMapping(value = "/customer/{customerEmailId}/product/{productId}")
    public ResponseEntity<String> modifyQuantityOfProductInCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId,
            @NotNull(message = "{product.id.absent}") @PathVariable("productId") Integer productId,
            @RequestBody Integer quantity) throws Exception {

        customerCartService.modifyQuantityOfProductInCart(customerEmailId, productId, quantity);
        String message = environment.getProperty("CustomerCartAPI.PRODUCT_QUANTITY_UPDATE_FROM_CART_SUCCESS");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/customer/{customerEmailId}/products")
    public ResponseEntity<String> deleteAllProductsFromCart(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
                    message = "{invalid.email.format}")
            @PathVariable("customerEmailId") String customerEmailId)
            throws Exception {
        log.info("Received a request to clear the cart of "+customerEmailId );

        customerCartService.deleteAllProductsFromCart(customerEmailId);
        String message = environment.getProperty("CustomerCartAPI.ALL_PRODUCTS_DELETED");
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

}
