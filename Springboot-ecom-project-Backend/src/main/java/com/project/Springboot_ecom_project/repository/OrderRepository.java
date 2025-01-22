package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
