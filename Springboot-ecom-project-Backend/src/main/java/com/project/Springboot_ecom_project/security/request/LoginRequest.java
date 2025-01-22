package com.project.Springboot_ecom_project.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
