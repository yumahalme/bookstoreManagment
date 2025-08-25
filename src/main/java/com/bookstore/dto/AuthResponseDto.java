package com.bookstore.dto;

import java.util.Set;

/**
 * Data Transfer Object for authentication responses.
 * 
 * This DTO provides:
 * - JWT token for subsequent requests
 * - User information
 * - User roles and permissions
 * - Token expiration information
 */
public class AuthResponseDto {

    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserInfoDto user;
    private Set<String> roles;

    // Constructors
    public AuthResponseDto() {}

    public AuthResponseDto(String token, Long expiresIn, UserInfoDto user, Set<String> roles) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.user = user;
        this.roles = roles;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfoDto getUser() {
        return user;
    }

    public void setUser(UserInfoDto user) {
        this.user = user;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AuthResponseDto{" +
                "token='" + (token != null ? "***" : null) + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", user=" + user +
                ", roles=" + roles +
                '}';
    }

    /**
     * Inner DTO for user information in auth response
     */
    public static class UserInfoDto {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;

        // Constructors
        public UserInfoDto() {}

        public UserInfoDto(Long id, String username, String email, String firstName, String lastName) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "UserInfoDto{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
}
