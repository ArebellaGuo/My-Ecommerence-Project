package com.project.Springboot_ecom_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Integer quantity;
    private Double orderedItemTotalPrice;
    private ProductDTO productDTO;
    private Double orderedItemPrice;
}