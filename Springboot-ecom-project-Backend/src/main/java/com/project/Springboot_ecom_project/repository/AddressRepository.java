package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query("SELECT ad FROM Address ad WHERE ad.user.userId = ?1")
    Page<Address> findByUserId(Long userId, Pageable pageable);
}
