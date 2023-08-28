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
    public Integer addProductToCart(CustomerCart customerCartDTO) {
        Set<CartProductEntity> cartProducts= new HashSet<>();
        Integer cartId = null;
        for(CartProduct cartProductDTO : customerCartDTO.getCartProducts()) {
            CartProductEntity cartProduct = new CartProductEntity();
            cartProduct.setProductId(cartProductDTO.getProduct().getProductId());
            cartProduct.setQuantity(cartProductDTO.getQuantity());
            cartProducts.add(cartProduct);
        }
        Optional<CustomerCartEntity> cartOptional = customerCartRepo
                .findByCustomerEmailId(customerCartDTO.getCustomerEmailId());
        if(cartOptional.isEmpty()) {
            CustomerCartEntity newCart = new  CustomerCartEntity();
            newCart.setCustomerEmailId(customerCartDTO.getCustomerEmailId());
            newCart.setCartProducts(cartProducts);
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
        Set<CartProduct> cartProductsDTO = new HashSet<>();
        CustomerCartEntity cart = cartOptional.orElseThrow(() ->
                new Exception(environment.getProperty("CustomerCartService.NO_CART_FOUND")));
        if (cart.getCartProducts().isEmpty()) {
            throw new Exception(environment.getProperty("CustomerCartService.NO_PRODUCT_ADDED_TO_CART"));
        }
        Set<CartProductEntity> cartProducts = cart.getCartProducts();
        for (CartProductEntity cartProduct : cartProducts) {
            CartProduct cartProductDTO = new CartProduct();
            cartProductDTO.setCartProductId(cartProduct.getCartProductId());
            cartProductDTO.setQuantity(cartProduct.getQuantity());
            Product productDTO = new Product();
            productDTO.setProductId(cartProduct.getProductId());
            cartProductDTO.setProduct(productDTO);
            cartProductsDTO.add(cartProductDTO);
        }
        return cartProductsDTO;
    }

    public void deleteProductFromCart(String customerEmailId, Integer productId) throws Exception {
        Optional<CustomerCartEntity> cartOptional = customerCartRepo.findByCustomerEmailId(customerEmailId);
        CustomerCartEntity cart = cartOptional.orElseThrow(() -> new Exception(
                environment.getProperty("CustomerCartService.NO_CART_FOUND")));
        if (cart.getCartProducts().isEmpty()) {
            throw new Exception(environment.getProperty("CustomerCartService.NO_PRODUCT_ADDED_TO_CART"));
        }
        customerCartRepo.delete(cart);
    }

    public void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity) {
    }

    public void deleteAllProductsFromCart(String customerEmailId) {
    }
}
