package com.project.Springboot_ecom_project.repository;

import com.project.Springboot_ecom_project.model.Role;
import com.project.Springboot_ecom_project.model.RoleConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleConstants appRole);
}
