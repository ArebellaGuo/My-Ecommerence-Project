package com.project.Springboot_ecom_project.config;

import com.project.Springboot_ecom_project.model.Role;
import com.project.Springboot_ecom_project.model.RoleConstants;
import com.project.Springboot_ecom_project.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, RoleConstants.ROLE_USER, new ArrayList<>()));
            roleRepository.save(new Role(null, RoleConstants.ROLE_SELLER, new ArrayList<>()));
            roleRepository.save(new Role(null, RoleConstants.ROLE_ADMIN, new ArrayList<>()));
        }
    }
}