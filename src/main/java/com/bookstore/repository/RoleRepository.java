package com.bookstore.repository;

import com.bookstore.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entities.
 * 
 * This repository provides:
 * - Standard CRUD operations through JpaRepository
 * - Custom query methods for role management
 * - Security-related queries
 * - Performance-optimized queries
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find role by name
     */
    Optional<Role> findByName(String name);

    /**
     * Find active role by name
     */
    Optional<Role> findByNameAndIsActiveTrue(String name);

    /**
     * Check if role name exists
     */
    boolean existsByName(String name);

    /**
     * Find all active roles
     */
    java.util.List<Role> findByIsActiveTrue();
}
