package com.project.Springboot_ecom_project.utils;

import com.project.Springboot_ecom_project.model.User;
import com.project.Springboot_ecom_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    @Autowired
    UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));
        return user.getEmail();

    }

    private Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));
        return user.getUserId();
    }

    public User loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));
        return user;

    }


}
