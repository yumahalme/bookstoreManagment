package com.bookstore.repository;

import com.bookstore.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entities.
 * 
 * This repository provides:
 * - Standard CRUD operations through JpaRepository
 * - Custom query methods for complex searches
 * - Pagination support for large datasets
 * - Performance-optimized queries with proper indexing
 * 
 * Design decisions:
 * - JPA repository for standard operations
 * - Custom queries for complex search scenarios
 * - Method naming conventions for automatic query generation
 * - @Query annotations for complex JPQL queries
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find books by title (case-insensitive partial match)
     */
    Page<Book> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title, Pageable pageable);

    /**
     * Find books by author name (case-insensitive partial match)
     */
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.authors a " +
           "WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%')) " +
           "AND b.isActive = true")
    Page<Book> findByAuthorNameContainingIgnoreCase(@Param("authorName") String authorName, Pageable pageable);

    /**
     * Find books by genre name (case-insensitive partial match)
     */
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.genres g " +
           "WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :genreName, '%')) " +
           "AND b.isActive = true")
    Page<Book> findByGenreNameContainingIgnoreCase(@Param("genreName") String genreName, Pageable pageable);

    /**
     * Find books by publisher (case-insensitive partial match)
     */
    Page<Book> findByPublisherContainingIgnoreCaseAndIsActiveTrue(String publisher, Pageable pageable);

    /**
     * Find books by language
     */
    Page<Book> findByLanguageAndIsActiveTrue(String language, Pageable pageable);

    /**
     * Find books published in a specific year
     */
    Page<Book> findByPublishedYearAndIsActiveTrue(Integer publishedYear, Pageable pageable);

    /**
     * Find books published between years
     */
    Page<Book> findByPublishedYearBetweenAndIsActiveTrue(Integer startYear, Integer endYear, Pageable pageable);

    /**
     * Find books within a price range
     */
    Page<Book> findByPriceBetweenAndIsActiveTrue(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Find books in stock (stock quantity > 0)
     */
    Page<Book> findByStockQuantityGreaterThanAndIsActiveTrue(Integer minStock, Pageable pageable);

    /**
     * Find books by ISBN
     */
    Optional<Book> findByIsbnAndIsActiveTrue(String isbn);

    /**
     * Check if ISBN exists
     */
    boolean existsByIsbnAndIsActiveTrue(String isbn);

    /**
     * Find books with low stock (below threshold)
     */
    @Query("SELECT b FROM Book b WHERE b.stockQuantity <= :threshold AND b.isActive = true")
    List<Book> findBooksWithLowStock(@Param("threshold") Integer threshold);

    /**
     * Count books by genre
     */
    @Query("SELECT COUNT(b) FROM Book b JOIN b.genres g WHERE g.name = :genreName AND b.isActive = true")
    long countByGenre(@Param("genreName") String genreName);

    /**
     * Count books by author
     */
    @Query("SELECT COUNT(b) FROM Book b JOIN b.authors a WHERE a.name = :authorName AND b.isActive = true")
    long countByAuthor(@Param("authorName") String authorName);

    /**
     * Find books by multiple criteria (complex search)
     */
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN b.authors a " +
           "LEFT JOIN b.genres g " +
           "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%'))) " +
           "AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%'))) " +
           "AND (:publisher IS NULL OR LOWER(b.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))) " +
           "AND (:language IS NULL OR b.language = :language) " +
           "AND (:minYear IS NULL OR b.publishedYear >= :minYear) " +
           "AND (:maxYear IS NULL OR b.publishedYear <= :maxYear) " +
           "AND (:minPrice IS NULL OR b.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR b.price <= :maxPrice) " +
           "AND (:inStock IS NULL OR (:inStock = true AND b.stockQuantity > 0) OR (:inStock = false AND b.stockQuantity <= 0)) " +
           "AND b.isActive = true")
    Page<Book> findBySearchCriteria(
            @Param("title") String title,
            @Param("author") String author,
            @Param("genre") String genre,
            @Param("publisher") String publisher,
            @Param("language") String language,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("inStock") Boolean inStock,
            Pageable pageable);

    /**
     * Find all active books
     */
    Page<Book> findByIsActiveTrue(Pageable pageable);

    /**
     * Find books by multiple genres
     */
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.genres g " +
           "WHERE g.name IN :genreNames AND b.isActive = true")
    Page<Book> findByGenresIn(@Param("genreNames") List<String> genreNames, Pageable pageable);

    /**
     * Find books by multiple authors
     */
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.authors a " +
           "WHERE a.name IN :authorNames AND b.isActive = true")
    Page<Book> findByAuthorsIn(@Param("authorNames") List<String> authorNames, Pageable pageable);
}
