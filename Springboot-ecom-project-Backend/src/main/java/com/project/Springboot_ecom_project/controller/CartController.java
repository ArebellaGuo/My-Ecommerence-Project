package com.project.Springboot_ecom_project.controller;
import com.project.Springboot_ecom_project.payload.CartDTO;
import com.project.Springboot_ecom_project.service.CartService;
import com.project.Springboot_ecom_project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOList = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOList, HttpStatus.OK);
    }


    @GetMapping("/user/carts/current")
    public ResponseEntity<CartDTO> getCurrentUserCart(){
        CartDTO cartDTO =cartService.getUserCart();
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    //?
    @PostMapping("/user/carts/current/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @PutMapping("/user/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,quantity);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }


    @DeleteMapping("/user/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }








}
