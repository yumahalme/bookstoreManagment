package com.bookstore.repository;

import com.bookstore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * 
 * This repository provides:
 * - Standard CRUD operations through JpaRepository
 * - Custom query methods for user authentication
 * - Security-related queries
 * - Performance-optimized queries
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find active user by username
     */
    Optional<User> findByUsernameAndIsActiveTrue(String username);

    /**
     * Find active user by email
     */
    Optional<User> findByEmailAndIsActiveTrue(String email);

    /**
     * Find users by role
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.isActive = true")
    java.util.List<User> findUsersByRole(@Param("roleName") String roleName);

    /**
     * Find users with multiple roles
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roleNames AND u.isActive = true")
    java.util.List<User> findUsersByRoles(@Param("roleNames") java.util.List<String> roleNames);

    /**
     * Count users by role
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.isActive = true")
    long countUsersByRole(@Param("roleName") String roleName);
}
