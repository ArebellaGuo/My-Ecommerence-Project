package com.project.Springboot_ecom_project.service;

import com.project.Springboot_ecom_project.payload.CartDTO;

import java.util.List;

public interface CartService {
    List<CartDTO> getAllCarts();

    CartDTO addProductToCart(Long productId, Integer quantity);

    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    CartDTO getUserCart();
}
