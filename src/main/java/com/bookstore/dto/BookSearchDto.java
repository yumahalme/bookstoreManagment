package com.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for book search parameters.
 * 
 * This DTO is used for:
 * - Search query parameters
 * - Pagination settings
 * - Filter criteria
 * - Sort options
 * 
 * Design decisions:
 * - Optional search parameters for flexible queries
 * - Pagination with reasonable defaults
 * - Sort options for result ordering
 * - Validation for pagination limits
 */
public class BookSearchDto {

    @Size(max = 100, message = "Title search term cannot exceed 100 characters")
    private String title;
    
    @Size(max = 100, message = "Author search term cannot exceed 100 characters")
    private String author;
    
    @Size(max = 50, message = "Genre search term cannot exceed 50 characters")
    private String genre;
    
    @Size(max = 100, message = "Publisher search term cannot exceed 100 characters")
    private String publisher;
    
    private String language;
    
    private Integer minYear;
    private Integer maxYear;
    
    private Double minPrice;
    private Double maxPrice;
    
    private Boolean inStock;
    
    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;
    
    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size = 20;
    
    private String sortBy = "title";
    private String sortDirection = "asc";

    // Constructors
    public BookSearchDto() {}

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    // Utility methods
    public boolean hasSearchCriteria() {
        return title != null || author != null || genre != null || 
               publisher != null || language != null || minYear != null || 
               maxYear != null || minPrice != null || maxPrice != null || 
               inStock != null;
    }

    public boolean isValidPriceRange() {
        if (minPrice != null && maxPrice != null) {
            return minPrice <= maxPrice;
        }
        return true;
    }

    public boolean isValidYearRange() {
        if (minYear != null && maxYear != null) {
            return minYear <= maxYear;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BookSearchDto{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", publisher='" + publisher + '\'' +
                ", language='" + language + '\'' +
                ", minYear=" + minYear +
                ", maxYear=" + maxYear +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", inStock=" + inStock +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                '}';
    }
}
