package com.bookstore.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for generating password hashes.
 * This is for development/testing purposes only.
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for admin123
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Password: " + adminPassword);
        System.out.println("Hash: " + adminHash);
        
        // Generate hash for user123
        String userPassword = "user123";
        String userHash = encoder.encode(userPassword);
        System.out.println("\nPassword: " + userPassword);
        System.out.println("Hash: " + userHash);
        
        // Verify the hashes
        System.out.println("\nVerification:");
        System.out.println("admin123 matches admin hash: " + encoder.matches(adminPassword, adminHash));
        System.out.println("user123 matches user hash: " + encoder.matches(userPassword, userHash));
    }
}
