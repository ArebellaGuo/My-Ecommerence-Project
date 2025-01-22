package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.userId = ?1")
    Cart findByUserId(Long userId);
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findByEmail(String email);
    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItemList ci JOIN FETCH ci.product p WHERE p.id = ?1")
    List<Cart> findCartsByProductId(Long productId);
}
