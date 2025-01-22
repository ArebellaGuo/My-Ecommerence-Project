package com.project.Springboot_ecom_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private LocalDate orderDate;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;

    private List<OrderItemDTO> orderItems;
    private PaymentDTO payment;
}

