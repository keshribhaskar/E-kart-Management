package com.assignment.ekart.ekartms.service;

import com.assignment.ekart.ekartms.entity.CartProductEntity;
import com.assignment.ekart.ekartms.entity.CustomerCartEntity;
import com.assignment.ekart.ekartms.model.CartProduct;
import com.assignment.ekart.ekartms.model.CustomerCart;
import com.assignment.ekart.ekartms.model.Product;
import com.assignment.ekart.ekartms.repository.CustomerCartRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
@Transactional
@Slf4j
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    Environment environment;
    @Autowired
    private CustomerCartRepo customerCartRepo;
    public Integer addProductToCart(CustomerCart customerCart) {

        Set<CartProductEntity> cartProducts= new HashSet<>();
        Integer cartId = null;
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
            cartId = newCart.getCartId();
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
            cartId = cart.getCartId();
        }
        return cartId;
    }

    public Set<CartProduct> getProductsFromCart(String customerEmailId) throws Exception {
        Optional<CustomerCartEntity> cartOptional = customerCartRepo
                .findByCustomerEmailId(customerEmailId);
        Set<CartProduct> cartProductsDetail = new HashSet<>();
        CustomerCartEntity cart = cartOptional.orElseThrow(() ->
                new Exception("No cart found"));
        if (cart.getCartProducts().isEmpty()) {
            throw new Exception("No product added to cart");
        }
        Set<CartProductEntity> cartProducts = cart.getCartProducts();
        for (CartProductEntity cartProduct : cartProducts) {

            Product product = Product.builder().productId(cartProduct.getProductId()).build();
            CartProduct cartProduct1 = CartProduct.builder()
                    .cartProductId(cartProduct.getCartProductId())
                    .quantity(cartProduct.getQuantity())
                    .product(product)
                    .build();
            cartProductsDetail.add(cartProduct1);

        }
        return cartProductsDetail;
    }

    public void deleteProductFromCart(String customerEmailId, Integer productId) throws Exception {
        Optional<CustomerCartEntity> cartOptional = customerCartRepo.findByCustomerEmailId(customerEmailId);
        CustomerCartEntity cart = cartOptional.orElseThrow(() -> new Exception(
                "No cart found"));
        if (cart.getCartProducts().isEmpty()) {
            throw new Exception("No product added to cart");
        }
        customerCartRepo.delete(cart);
    }
}
