package com.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for authentication requests.
 * 
 * This DTO is used for:
 * - Login requests
 * - Input validation
 * - Secure credential handling
 * - API authentication
 */
public class AuthRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public AuthRequestDto() {}

    public AuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "AuthRequestDto{" +
                "username='" + username + '\'' +
                '}';
    }
}
