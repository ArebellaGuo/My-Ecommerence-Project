package com.project.Springboot_ecom_project.controller;

import com.project.Springboot_ecom_project.model.Role;
import com.project.Springboot_ecom_project.model.RoleConstants;
import com.project.Springboot_ecom_project.model.User;
import com.project.Springboot_ecom_project.repository.RoleRepository;
import com.project.Springboot_ecom_project.repository.UserRepository;
import com.project.Springboot_ecom_project.security.jwt.JwtUtils;
import com.project.Springboot_ecom_project.security.request.LoginRequest;
import com.project.Springboot_ecom_project.security.request.SignupRequest;
import com.project.Springboot_ecom_project.security.response.UserInfoResponse;
import com.project.Springboot_ecom_project.security.services.UserDetailsImpl;
import com.project.Springboot_ecom_project.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDetailsServiceImpl userDetails;



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(RoleConstants.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }else{
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(RoleConstants.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByRoleName(RoleConstants.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(RoleConstants.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");

    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("Input password: " + loginRequest.getPassword());
        System.out.println("Input password length: " + loginRequest.getPassword().length());
        UserDetails userDetails1 = userDetails.loadUserByUsername(loginRequest.getUsername());
        System.out.printf("password from userdetails"+userDetails1.getPassword());


        System.out.println("=====Password matches=====: " + encoder.matches(loginRequest.getPassword(), userDetails1.getPassword()));
        System.out.println("Password length: " + userDetails1.getPassword().length());



        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));

        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails =(UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles, jwtCookie.toString());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        jwtCookie.toString())
                .body(response);

    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body("You've been signed out!");
    }
}
