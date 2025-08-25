package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for the Bookstore Inventory Management System.
 * 
 * This application provides a REST API for managing bookstore inventory,
 * including CRUD operations for books, search functionality, and user authentication.
 * 
 * Key architectural decisions:
 * - Spring Boot 3.x for modern Java features and performance
 * - JPA auditing for automatic timestamp management
 * - Async processing capability for future scalability
 * - RESTful API design with proper versioning
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookstoreInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreInventoryApplication.class, args);
    }
}
