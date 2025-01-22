package com.project.Springboot_ecom_project.security.services;

import com.project.Springboot_ecom_project.model.User;
import com.project.Springboot_ecom_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUserName(username)
               .orElseThrow(()->new UsernameNotFoundException("User not found with username:"+username));
        System.out.println("Loaded user: " + user.getUserName() + ", password: " + user.getPassword());
        System.out.println("Password length: " + user.getPassword().length());
        return UserDetailsImpl.build(user);
    }
}
