package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(@Email String email);
}
