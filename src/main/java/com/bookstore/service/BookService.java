package com.bookstore.service;

import com.bookstore.domain.Book;
import com.bookstore.dto.BookDto;
import com.bookstore.dto.BookSearchDto;
import com.bookstore.dto.PageResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Book-related business operations.
 * 
 * This service provides:
 * - Business logic for book management
 * - CRUD operations for books
 * - Search and filtering capabilities
 * - Inventory management
 * - Data validation and business rules
 * 
 * Design decisions:
 * - Interface-based design for testability
 * - Separation of concerns between layers
 * - Business logic encapsulation
 * - Transaction management
 */
public interface BookService {

    /**
     * Create a new book
     */
    BookDto createBook(BookDto bookDto);

    /**
     * Update an existing book
     */
    BookDto updateBook(Long id, BookDto bookDto);

    /**
     * Get a book by ID
     */
    Optional<BookDto> getBookById(Long id);

    /**
     * Get a book by ISBN
     */
    Optional<BookDto> getBookByIsbn(String isbn);

    /**
     * Get all books with pagination
     */
    PageResponseDto<BookDto> getAllBooks(int page, int size);

    /**
     * Search books by criteria
     */
    PageResponseDto<BookDto> searchBooks(BookSearchDto searchDto);

    /**
     * Delete a book (soft delete)
     */
    void deleteBook(Long id);

    /**
     * Restore a deleted book
     */
    BookDto restoreBook(Long id);

    /**
     * Update book stock quantity
     */
    BookDto updateStockQuantity(Long id, Integer newQuantity);

    /**
     * Get books with low stock
     */
    List<BookDto> getBooksWithLowStock(int threshold);

    /**
     * Get books by genre
     */
    PageResponseDto<BookDto> getBooksByGenre(String genreName, int page, int size);

    /**
     * Get books by author
     */
    PageResponseDto<BookDto> getBooksByAuthor(String authorName, int page, int size);

    /**
     * Get books by publisher
     */
    PageResponseDto<BookDto> getBooksByPublisher(String publisher, int page, int size);

    /**
     * Get books in a price range
     */
    PageResponseDto<BookDto> getBooksByPriceRange(Double minPrice, Double maxPrice, int page, int size);

    /**
     * Get books published in a year range
     */
    PageResponseDto<BookDto> getBooksByYearRange(Integer startYear, Integer endYear, int page, int size);

    /**
     * Get books in stock
     */
    PageResponseDto<BookDto> getBooksInStock(int page, int size);

    /**
     * Get books out of stock
     */
    PageResponseDto<BookDto> getBooksOutOfStock(int page, int size);

    /**
     * Get book statistics
     */
    BookStatistics getBookStatistics();

    /**
     * Check if book exists by ISBN
     */
    boolean existsByIsbn(String isbn);

    /**
     * Get book count by genre
     */
    long getBookCountByGenre(String genreName);

    /**
     * Get book count by author
     */
    long getBookCountByAuthor(String authorName);

    /**
     * Inner class for book statistics
     */
    class BookStatistics {
        private long totalBooks;
        private long booksInStock;
        private long booksOutOfStock;
        private long totalGenres;
        private long totalAuthors;
        private double averagePrice;
        private double totalInventoryValue;

        // Constructors
        public BookStatistics() {}

        public BookStatistics(long totalBooks, long booksInStock, long booksOutOfStock, 
                           long totalGenres, long totalAuthors, double averagePrice, double totalInventoryValue) {
            this.totalBooks = totalBooks;
            this.booksInStock = booksInStock;
            this.booksOutOfStock = booksOutOfStock;
            this.totalGenres = totalGenres;
            this.totalAuthors = totalAuthors;
            this.averagePrice = averagePrice;
            this.totalInventoryValue = totalInventoryValue;
        }

        // Getters and Setters
        public long getTotalBooks() { return totalBooks; }
        public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }

        public long getBooksInStock() { return booksInStock; }
        public void setBooksInStock(long booksInStock) { this.booksInStock = booksInStock; }

        public long getBooksOutOfStock() { return booksOutOfStock; }
        public void setBooksOutOfStock(long booksOutOfStock) { this.booksOutOfStock = booksOutOfStock; }

        public long getTotalGenres() { return totalGenres; }
        public void setTotalGenres(long totalGenres) { this.totalGenres = totalGenres; }

        public long getTotalAuthors() { return totalAuthors; }
        public void setTotalAuthors(long totalAuthors) { this.totalAuthors = totalAuthors; }

        public double getAveragePrice() { return averagePrice; }
        public void setAveragePrice(double averagePrice) { this.averagePrice = averagePrice; }

        public double getTotalInventoryValue() { return totalInventoryValue; }
        public void setTotalInventoryValue(double totalInventoryValue) { this.totalInventoryValue = totalInventoryValue; }
    }
}
