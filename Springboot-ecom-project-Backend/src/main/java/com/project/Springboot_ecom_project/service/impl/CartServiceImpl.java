package com.project.Springboot_ecom_project.service.impl;

import com.project.Springboot_ecom_project.exception.APIException;
import com.project.Springboot_ecom_project.exception.ResourceNotFound;
import com.project.Springboot_ecom_project.model.*;
import com.project.Springboot_ecom_project.payload.CartDTO;
import com.project.Springboot_ecom_project.payload.CategoryDTO;
import com.project.Springboot_ecom_project.payload.ProductDTO;
import com.project.Springboot_ecom_project.repository.CartItemRepository;
import com.project.Springboot_ecom_project.repository.CartRepository;
import com.project.Springboot_ecom_project.repository.ProductRepository;
import com.project.Springboot_ecom_project.repository.UserRepository;
import com.project.Springboot_ecom_project.service.CartService;
import com.project.Springboot_ecom_project.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cartList = cartRepository.findAll();
        if (cartList.isEmpty()) {
            throw new APIException("No cart exists!");
        }

        List<CartDTO> cartDTOList = cartList.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            //get cartitem list from cart
            List<CartItem> cartItemList = cart.getCartItemList();
            List<ProductDTO> productDTOList = cartItemList.stream().map(cartItem -> {
                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity());
                Category productCategory = cartItem.getProduct().getCategory();
                CategoryDTO categoryDTO = modelMapper.map(productCategory, CategoryDTO.class);
                productDTO.setCategoryDTO(categoryDTO);
                return productDTO;
            }).toList();
            cartDTO.setProductDTOList(productDTOList);
            return cartDTO;
        }).toList();
        return cartDTOList;
    }


    @Override
    public CartDTO getUserCart() {
        //get user and its cart
        User user = authUtil.loggedInUser();
        Cart cart = getCurrentUserCart(user);

        //convert cart to DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        //add productDTO to cartDTO
        //get cartitem list and then convert each item to productDTO
        List<CartItem> cartItemList = cart.getCartItemList();
        List<ProductDTO> productDTOList = cartItemList.stream().map(cartItem -> {
            //convert each cartItem to ProductDTO
            ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
            productDTO.setQuantity(cartItem.getQuantity());
            Category category = cartItem.getProduct().getCategory();
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            productDTO.setCategoryDTO(categoryDTO);
            return productDTO;
        }).toList();

        //add productDTO to cartDTO
        cartDTO.setProductDTOList(productDTOList);
        return cartDTO;
    }

    private Cart getCurrentUserCart(User user) {
        Cart userCart = cartRepository.findByUserId(user.getUserId());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalCartPrice(0.00);
        cart.setUser(user);
        return cart = cartRepository.save(cart);
    }


    @Override
    @Transactional
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //get product by productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product", productId));
        //validate if product has sufficient stock
        if (quantity > product.getQuantity()) {
            throw new APIException("Product stock is not sufficient! You can only add " + product.getQuantity() + " products in maxim!");
        };
        if (quantity < 0 || quantity == 0) {
            throw new APIException("Failed to add product!");
        };

        //get current user cart
        User user = authUtil.loggedInUser();
        Cart cart = getCurrentUserCart(user);

        //validate if product exists in cartitemlist
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        //if product is new for the cart,convert product to a cartitem
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        //added item quantity
        newCartItem.setQuantity(quantity);
        //single product price
        newCartItem.setCartItemPrice(product.getPrice());
        newCartItem.setCartItemTotalPrice(newCartItem.getCartItemPrice() * quantity);
        newCartItem.setCart(cart);
        cartItemRepository.save(newCartItem);
        //update product stock quantity after being added
        product.setQuantity(product.getQuantity() - quantity);
        //save updated product
        productRepository.save(product);
        //add product to the list
        //cartItemList.add(newCartItem);

        //update new cartitemlist to the cart
        //cart.setCartItemList(cartItemList);

        //update cart total price
        List<CartItem> cartItemList = cart.getCartItemList();
        double totalPrice = 0.00;
        for (CartItem item : cartItemList) {
            totalPrice += item.getCartItemTotalPrice();
        }
        cart.setTotalCartPrice(totalPrice);
        //save updated cart
        cartRepository.save(cart);

        //convert cart to DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        //convert cartitem list to productDTO list
        List<ProductDTO> productDTOList = cartItemList.stream().map(cartEachItem -> {
            ProductDTO productDTO = modelMapper.map(cartEachItem.getProduct(), ProductDTO.class);
            Category category = cartEachItem.getProduct().getCategory();
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            productDTO.setQuantity(cartEachItem.getQuantity());
            productDTO.setCategoryDTO(categoryDTO);
            return productDTO;
        }).toList();
        //add ProductDTO to cartDTO
        cartDTO.setProductDTOList(productDTOList);


        return cartDTO;
    }


    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        User user = authUtil.loggedInUser();
        Cart cart = getCurrentUserCart(user);

        //find product in repository
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product", productId));

        //find product in cart
        List<CartItem> cartItemList = cart.getCartItemList();
        CartItem savedProductCartItem = cartItemList.stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Product", productId));

        //validate if product has enough stock
        if (product.getQuantity() == 0) {
            throw new APIException("No product available in the stock now!");
        }
        if (quantity > product.getQuantity()) {
            throw new APIException("You can only add at maxim " + product.getQuantity() + " products!");
        }
        if (quantity + savedProductCartItem.getQuantity()< 0 ) {
            throw new APIException("Fail to update product!");
        }
        //if updated cartitem quantity is 0 ,remove this item from list
        if (quantity == 0 || (quantity + savedProductCartItem.getQuantity() == 0)) {
            cartItemList.remove(savedProductCartItem);
        }

        //update product quantity
        savedProductCartItem.setQuantity(quantity);
        savedProductCartItem.setCartItemPrice(product.getPrice());
        savedProductCartItem.setCartItemTotalPrice(savedProductCartItem.getCartItemPrice() * quantity);

        //update saved product in repository
        cartItemRepository.save(savedProductCartItem);


        //update changedProductitem in the cartitemlist in cart
        cart.setCartItemList(cart.getCartItemList());
        //update cart item price
        double totalPrice = 0.00;
        for (CartItem cartItem : cartItemList) {
            totalPrice += cartItem.getCartItemTotalPrice();
        }
        cart.setTotalCartPrice(totalPrice);
        cartRepository.save(cart);

        //update product stock
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        //convert cart to cartdto
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        //cartDTO.setTotalPrice(cart.getTotalCartPrice());
        List<ProductDTO> productDTOList = cartItemList.stream().map(cartItem -> {
            ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
            productDTO.setQuantity(cartItem.getQuantity());
            CategoryDTO categoryDTO = modelMapper.map(cartItem.getProduct().getCategory(), CategoryDTO.class);
            productDTO.setCategoryDTO(categoryDTO);
            return productDTO;
        }).toList();
        cartDTO.setProductDTOList(productDTOList);
        return cartDTO;
    }


    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFound("Cart", cartId));

//        CartItem cartItem = cart.getCartItemList().stream()
//                .filter(item -> item.getProduct().getProductId().equals(productId))
//                .findFirst()
//                .orElseThrow(() -> new ResourceNotFound("Product", productId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFound("Product",  productId);
        }

        double priceToDeduct = cartItem.getCartItemPrice() * cartItem.getQuantity();
        cart.setTotalCartPrice(cart.getTotalCartPrice() - priceToDeduct);

        cart.getCartItemList().remove(cartItem);

        //cartItemRepository.delete(cartItem);
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        cartRepository.save(cart);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart!";
    }


}



