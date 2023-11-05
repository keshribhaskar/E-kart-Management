package com.assignment.ekart.ekartms.service;

import com.assignment.ekart.ekartms.entity.CartProductEntity;
import com.assignment.ekart.ekartms.entity.CustomerCartEntity;
import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.model.Product;
import com.assignment.ekart.ekartms.repository.CustomerCartRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.assignment.ekart.ekartms.config.Constant.*;

@Service
@Transactional
@Slf4j
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    Environment environment;
    @Autowired
    private CustomerCartRepo customerCartRepo;
    @Autowired
    private RestTemplate template;
    @Value("${application.product.name}")
    private String productApp;

    public String addProductToCart(CustomerCart customerCart) {

        try{

            Set<CartProductEntity> cartProducts= new HashSet<>();
            for(CartProduct cartProductDetail : customerCart.getCartProducts()) {
                CartProductEntity cartProduct = CartProductEntity.builder()
                        .productId(cartProductDetail.getProduct().getProductId())
                        .quantity(cartProductDetail.getQuantity())
                        .build();
                cartProducts.add(cartProduct);
            }
            Optional<CustomerCartEntity> cartOptional = customerCartRepo
                    .findByCustomerEmailId(customerCart.getCustomerEmailId());
            if(cartOptional.isEmpty()) {
                CustomerCartEntity newCart = CustomerCartEntity.builder()
                        .customerEmailId(customerCart.getCustomerEmailId())
                        .cartProducts(cartProducts)
                        .build();
                customerCartRepo.save(newCart);
            }
            else {
                CustomerCartEntity cart = cartOptional.get();
                for(CartProductEntity cartProductToBeAdded: cartProducts) {
                    boolean checkProductAlreadyPresent =false;
                    for(CartProductEntity cartProductFromCart: cart.getCartProducts()) {
                        if(cartProductFromCart.equals(cartProductToBeAdded)) {
                            cartProductFromCart.setQuantity(cartProductToBeAdded.getQuantity()
                                    + cartProductFromCart.getQuantity());
                            checkProductAlreadyPresent=true;
                        }
                    }
                    if(checkProductAlreadyPresent == false) {
                        cart.getCartProducts().add(cartProductToBeAdded);
                    }
                }
            }
            return CART_ADD_SUCCESS;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public CustomerCart getProductsFromCart(String customerEmailId) throws Exception {
        try{
            Optional<CustomerCartEntity> cartOptional = customerCartRepo
                .findByCustomerEmailId(customerEmailId);
            Set<CartProduct> cartProductsDetail = new HashSet<>();
            CustomerCartEntity cart = cartOptional.orElseThrow(() ->
                    new Exception(CART_NOT_FOUND));
            if (cart.getCartProducts().isEmpty()) {
                throw new Exception(PRODUCT_NOT_ADDED);
            }
            Set<CartProductEntity> cartProducts = cart.getCartProducts();
            for (CartProductEntity cartProduct : cartProducts) {
                Product product = Product.builder().productId(cartProduct.getProductId()).build();
                CartProduct cartProduct1 = CartProduct.builder()
                        .quantity(cartProduct.getQuantity())
                        .product(product)
                        .build();
                cartProductsDetail.add(cartProduct1);
            }
            cartProductsDetail = getProducts(cartProductsDetail);
            Optional<CartProduct> errorState = cartProductsDetail.stream().findFirst();
            String status = errorState.get().getProduct().getError();
            if(!StringUtils.isEmpty(status)){
                throw new Exception(status);
            }else{
                CustomerCart customerCart = CustomerCart.builder().cartId(cart.getCartId())
                        .customerEmailId(cart.getCustomerEmailId())
                        .cartProducts(cartProductsDetail).build();
                return customerCart;
            }
        }catch (Exception e){
            return CustomerCart.builder().error(e.getMessage()).build();
        }
    }

    public String deleteProductFromCart(String customerEmailId) throws Exception {
        try{

            Optional<CustomerCartEntity> cartOptional = customerCartRepo.findByCustomerEmailId(customerEmailId);
            CustomerCartEntity cart = cartOptional.orElseThrow(() -> new Exception(
                    CART_NOT_FOUND));
            if (cart.getCartProducts().isEmpty()) {
                throw new Exception(PRODUCT_NOT_ADDED);
            }
            customerCartRepo.delete(cart);
            return CART_DELETE_SUCCESS;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public Set<CartProduct> getProducts(Set<CartProduct> cartProducts){
        try{
            for (CartProduct cartProduct : cartProducts) {
                ResponseEntity<Product> response = template.getForEntity(HTTP_HEAD + productApp + PRODUCT_ENDPOINT
                        + cartProduct.getProduct().getProductId(), Product.class);
                Product product = response.getBody();
                cartProduct.setProduct(product);
            }
            return  cartProducts;
        }catch (Exception e){
            log.info(e.getMessage());
            for (CartProduct cartProduct : cartProducts) {
                cartProduct.setProduct(Product.builder().error(e.getMessage()).build());
            }
            return  cartProducts;
        }
    }
}
