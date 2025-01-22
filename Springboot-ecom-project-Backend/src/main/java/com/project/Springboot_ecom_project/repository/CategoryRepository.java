package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.Category;
import com.project.Springboot_ecom_project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);


}
