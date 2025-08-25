package com.bookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Author entity representing book authors in the system.
 * 
 * Design decisions:
 * - Separate entity for authors to enable author-based search and management
 * - Many-to-many relationship with books for flexibility
 * - Validation constraints for data integrity
 * - Bidirectional relationship for efficient querying
 * 
 * Future considerations:
 * - Author biography and photo
 * - Author awards and recognition
 * - Author social media links
 * - Author events and book signings
 */
@Entity
@Table(name = "authors", indexes = {
    @Index(name = "idx_author_name", columnList = "name"),
    @Index(name = "idx_author_email", columnList = "email")
})
public class Author extends BaseEntity {

    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "Biography cannot exceed 500 characters")
    @Column(name = "biography", length = 500)
    private String biography;

    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Size(max = 50, message = "Country cannot exceed 50 characters")
    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "birth_year")
    private Integer birthYear;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();

    // Constructors
    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
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
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }
}
