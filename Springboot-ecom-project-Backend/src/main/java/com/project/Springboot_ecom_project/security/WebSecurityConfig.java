package com.project.Springboot_ecom_project.security;

import com.project.Springboot_ecom_project.model.RoleConstants;
import com.project.Springboot_ecom_project.security.jwt.JwtTokenFilter;
import com.project.Springboot_ecom_project.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/admin/**").hasAuthority(RoleConstants.ROLE_ADMIN.name())
                        .requestMatchers("/api/seller/**").hasAuthority(RoleConstants.ROLE_SELLER.name())
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();

        daoAuthProvider.setUserDetailsService(userDetailsService);
        daoAuthProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
