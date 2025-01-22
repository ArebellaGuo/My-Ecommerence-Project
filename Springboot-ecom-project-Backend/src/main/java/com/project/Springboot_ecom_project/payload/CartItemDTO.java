package com.project.Springboot_ecom_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Integer quantity;
    private Double cartItemPrice;
    private Double cartItemTotalPrice;
    private long cartId;
    private String productName;
}
