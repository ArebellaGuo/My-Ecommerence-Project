package com.project.Springboot_ecom_project.controller;

import com.project.Springboot_ecom_project.payload.OrderDTO;
import com.project.Springboot_ecom_project.payload.OrderRequestDTO;
import com.project.Springboot_ecom_project.service.OrderService;
import com.project.Springboot_ecom_project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private OrderService orderService;

    @PostMapping("/user/order/payment/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @RequestBody OrderRequestDTO orderRequestDTO){
        OrderDTO orderDTO = orderService.placeOrder(
                authUtil.loggedInEmail(),
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }


}
