package com.bookstore.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Data Transfer Object for Book entities.
 * 
 * This DTO is used for:
 * - API request/response serialization
 * - Input validation
 * - Data transformation between layers
 * - API versioning support
 * 
 * Design decisions:
 * - Separate DTOs for create, update, and response operations
 * - Validation annotations for input validation
 * - Immutable design for thread safety
 * - Builder pattern for object construction
 */
public class BookDto {

    private Long id;
    
    @NotBlank(message = "Book title is required")
    @Size(min = 1, max = 200, message = "Book title must be between 1 and 200 characters")
    private String title;
    
    @Size(max = 13, message = "ISBN must be 10 or 13 characters")
    private String isbn;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "Price must have up to 6 digits before decimal and 2 after")
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    
    @Min(value = 1800, message = "Published year must be 1800 or later")
    @Max(value = 2100, message = "Published year cannot be in the future")
    private Integer publishedYear;
    
    @Size(max = 100, message = "Publisher cannot exceed 100 characters")
    private String publisher;
    
    @Size(max = 50, message = "Language cannot exceed 50 characters")
    private String language;
    
    @Min(value = 1, message = "Page count must be at least 1")
    private Integer pageCount;
    
    private Boolean isActive;
    private Set<AuthorDto> authors;
    private Set<GenreDto> genres;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public BookDto() {}

    public BookDto(String title, BigDecimal price, Integer stockQuantity) {
        this.title = title;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDto> authors) {
        this.authors = authors;
    }

    public Set<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDto> genres) {
        this.genres = genres;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", publishedYear=" + publishedYear +
                ", publisher='" + publisher + '\'' +
                ", language='" + language + '\'' +
                ", pageCount=" + pageCount +
                ", isActive=" + isActive +
                '}';
    }
}
