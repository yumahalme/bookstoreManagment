package com.bookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Book entity representing books in the bookstore inventory.
 * 
 * Design decisions:
 * - Many-to-many relationships with authors and genres for flexibility
 * - Comprehensive validation for data integrity
 * - Inventory tracking with stock quantity
 * - Price management with decimal precision
 * - ISBN validation for uniqueness
 * - Soft delete capability for audit trails
 * 
 * Future considerations:
 * - Book cover images and media
 * - Book reviews and ratings
 * - Book series and sequels
 * - Publisher information
 * - Book condition tracking
 * - E-book availability
 */
@Entity
@Table(name = "books", indexes = {
    @Index(name = "idx_book_title", columnList = "title"),
    @Index(name = "idx_book_isbn", columnList = "isbn"),
    @Index(name = "idx_book_price", columnList = "price"),
    @Index(name = "idx_book_stock", columnList = "stock_quantity"),
    @Index(name = "idx_book_published_year", columnList = "published_year")
})
public class Book extends BaseEntity {

    @NotBlank(message = "Book title is required")
    @Size(min = 1, max = 200, message = "Book title must be between 1 and 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 13, message = "ISBN must be 10 or 13 characters")
    @Column(name = "isbn", length = 13, unique = true)
    private String isbn;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 6, fraction = 2, message = "Price must have up to 6 digits before decimal and 2 after")
    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Min(value = 1800, message = "Published year must be 1800 or later")
    @Max(value = 2100, message = "Published year cannot be in the future")
    @Column(name = "published_year")
    private Integer publishedYear;

    @Size(max = 100, message = "Publisher cannot exceed 100 characters")
    @Column(name = "publisher", length = 100)
    private String publisher;

    @Size(max = 50, message = "Language cannot exceed 50 characters")
    @Column(name = "language", length = 50)
    private String language = "English";

    @Min(value = 1, message = "Page count must be at least 1")
    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "book_genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    // Constructors
    public Book() {}

    public Book(String title, BigDecimal price, Integer stockQuantity) {
        this.title = title;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    // Utility methods
    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getBooks().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getBooks().remove(this);
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean isLowStock(int threshold) {
        return stockQuantity <= threshold;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
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
