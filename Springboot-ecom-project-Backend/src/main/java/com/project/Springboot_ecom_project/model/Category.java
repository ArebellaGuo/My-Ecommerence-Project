package com.project.Springboot_ecom_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories",
        uniqueConstraints  = {@UniqueConstraint(columnNames = "category_name")})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(name = "category_name", unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> productList;



}

