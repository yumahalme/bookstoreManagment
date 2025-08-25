package com.bookstore.dto;

/**
 * DTO for login responses.
 */
public class LoginResponse {

    private String token;
    private String username;
    private String authorities;
    private String message;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, String username, String authorities, String message) {
        this.token = token;
        this.username = username;
        this.authorities = authorities;
        this.message = message;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
