package com.project.Springboot_ecom_project.payload;

import com.project.Springboot_ecom_project.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;
    private CategoryDTO categoryDTO;



}
