package com.bookstore;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTest {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "admin123";
        String hash = "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG";
        
        System.out.println("Testing password: " + password);
        System.out.println("Testing hash: " + hash);
        System.out.println("Password matches hash: " + encoder.matches(password, hash));
        
        // Generate a new hash for comparison
        String newHash = encoder.encode(password);
        System.out.println("New hash generated: " + newHash);
        System.out.println("Password matches new hash: " + encoder.matches(password, newHash));
    }
}
