package com.bookstore.service;

import com.bookstore.dto.AuthRequestDto;
import com.bookstore.dto.AuthResponseDto;

/**
 * Service interface for authentication and authorization operations.
 * 
 * This service provides:
 * - User authentication
 * - JWT token generation and validation
 * - User registration
 * - Password management
 * - Role-based access control
 * 
 * Design decisions:
 * - Interface-based design for testability
 * - Separation of concerns between layers
 * - Security-focused operations
 * - Token-based authentication
 */
public interface AuthService {

    /**
     * Authenticate user and generate JWT token
     */
    AuthResponseDto authenticate(AuthRequestDto authRequest);

    /**
     * Validate JWT token
     */
    boolean validateToken(String token);

    /**
     * Get username from JWT token
     */
    String getUsernameFromToken(String token);

    /**
     * Get user roles from JWT token
     */
    java.util.Set<String> getRolesFromToken(String token);

    /**
     * Refresh JWT token
     */
    AuthResponseDto refreshToken(String token);

    /**
     * Logout user (invalidate token)
     */
    void logout(String token);

    /**
     * Check if user has specific role
     */
    boolean hasRole(String username, String roleName);

    /**
     * Check if user has any of the specified roles
     */
    boolean hasAnyRole(String username, java.util.Set<String> roleNames);

    /**
     * Check if user has permission to access resource
     */
    boolean hasPermission(String username, String resource, String action);
}
