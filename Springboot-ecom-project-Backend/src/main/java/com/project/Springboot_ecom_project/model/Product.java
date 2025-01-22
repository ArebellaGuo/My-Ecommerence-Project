package com.project.Springboot_ecom_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> cartItemList= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItem= new ArrayList<>();
}
