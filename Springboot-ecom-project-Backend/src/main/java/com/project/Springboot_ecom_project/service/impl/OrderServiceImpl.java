package com.project.Springboot_ecom_project.service.impl;

import com.project.Springboot_ecom_project.exception.APIException;
import com.project.Springboot_ecom_project.exception.ResourceNotFound;
import com.project.Springboot_ecom_project.model.*;
import com.project.Springboot_ecom_project.payload.CartItemDTO;
import com.project.Springboot_ecom_project.payload.OrderDTO;
import com.project.Springboot_ecom_project.payload.OrderItemDTO;
import com.project.Springboot_ecom_project.payload.PaymentDTO;
import com.project.Springboot_ecom_project.repository.*;
import com.project.Springboot_ecom_project.service.CartService;
import com.project.Springboot_ecom_project.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public OrderDTO placeOrder(String email, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        //find cart
        Cart cart = cartRepository.findByEmail(email);
        if (cart == null) {
            throw new APIException("Cart is not found!");
        }

        //validate address in repository
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFound("Address", addressId));

        //create order
        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalCartPrice());
        order.setOrderStatus("Order accepted!");
        order.setAddress(address);
        //create payment method
        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        //save payment
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        //get cartitems list from cart
        List<CartItem> cartItemList = cart.getCartItemList();
        if (cartItemList.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        //convert cartitems to orderitems
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            Product productFromDB = productRepository.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFound("Product", cartItem.getProduct().getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedItemPrice(cartItem.getCartItemPrice());
            orderItem.setOrderedItemTotalPrice(cartItem.getCartItemTotalPrice());
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(productFromDB);
            // Add to mutable list
            orderItemList.add(orderItem);
        }

        // Save the order item
        orderItemList = orderItemRepository.saveAll(orderItemList);

        order.setOrderItemList(orderItemList);

        // Update product stock (modify separately to avoid ConcurrentModificationException)
        List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItemList()); // Create a copy
        cartItemsCopy.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setQuantity(product.getQuantity() - quantity);

            // Save product back to the database
            productRepository.save(product);

            // Remove items from cart
            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        });

        //convert orderitem to DTO
        List<OrderItemDTO> orderItemDTOS = orderItemList.stream().map(orderItem ->
                modelMapper.map(orderItem, OrderItemDTO.class)).toList();

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderDTO.setOrderItems(orderItemDTOS);

        PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
        orderDTO.setPayment(paymentDTO);

        return orderDTO;
    }



}
