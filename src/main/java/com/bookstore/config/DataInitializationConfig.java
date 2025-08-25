package com.bookstore.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;

/**
 * Configuration for data initialization.
 *
 * This configuration ensures that:
 * - Data initialization happens after JPA setup
 * - Sample data is loaded for development
 * - Configuration is profile-specific
 */
@Configuration
@Profile("dev")
@ConditionalOnProperty(name = "spring.jpa.hibernate.ddl-auto", havingValue = "create-drop")
public class DataInitializationConfig {

    /**
     * Backup data initializer using CommandLineRunner
     * This will run after the application context is fully initialized
     */
    @Bean
    public CommandLineRunner dataInitializer(DataSource dataSource) {
        return args -> {
            try {
                System.out.println("=== Starting Data Initialization ===");
                System.out.println("=== Using CommandLineRunner for data.sql execution ===");
                
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("data.sql"));
                populator.setContinueOnError(true);
                
                System.out.println("=== Executing data.sql script... ===");
                populator.execute(dataSource);
                
                System.out.println("=== Data Initialization Completed Successfully ===");
            } catch (Exception e) {
                System.err.println("=== Data Initialization Failed ===");
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
