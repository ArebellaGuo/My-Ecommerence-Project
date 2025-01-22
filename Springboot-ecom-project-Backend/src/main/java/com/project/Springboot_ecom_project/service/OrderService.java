package com.project.Springboot_ecom_project.service;

import com.project.Springboot_ecom_project.payload.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String email, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
