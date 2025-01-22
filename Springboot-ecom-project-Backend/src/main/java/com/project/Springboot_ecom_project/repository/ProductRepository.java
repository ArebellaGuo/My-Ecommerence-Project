package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = ?1")
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = ?1 AND p.productName =?2")
    Optional<Product> findByCategoryIdAndProductName(Long categoryId, String productName);
}
