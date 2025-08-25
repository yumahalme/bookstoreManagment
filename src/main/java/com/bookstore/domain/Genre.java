package com.bookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Genre entity representing book genres in the system.
 * 
 * Design decisions:
 * - Separate entity for genres to enable genre-based categorization and search
 * - Many-to-many relationship with books for flexibility
 * - Validation constraints for data integrity
 * - Hierarchical structure support for future genre subcategories
 * 
 * Future considerations:
 * - Genre hierarchy (e.g., Fiction -> Mystery -> Detective)
 * - Genre popularity metrics
 * - Genre-based recommendations
 * - Genre-specific pricing strategies
 */
@Entity
@Table(name = "genres", indexes = {
    @Index(name = "idx_genre_name", columnList = "name"),
    @Index(name = "idx_genre_category", columnList = "category")
})
public class Genre extends BaseEntity {

    @NotBlank(message = "Genre name is required")
    @Size(min = 2, max = 50, message = "Genre name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    @Column(name = "description", length = 200)
    private String description;

    @Size(max = 50, message = "Category cannot exceed 50 characters")
    @Column(name = "category", length = 50)
    private String category; // e.g., "Fiction", "Non-Fiction", "Children's"

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    // Constructors
    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public Genre(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // Utility methods
    public void addBook(Book book) {
        this.books.add(book);
        book.getGenres().add(this);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
        book.getGenres().remove(this);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
