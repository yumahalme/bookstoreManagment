package com.bookstore.repository;

import com.bookstore.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Genre entities.
 * 
 * This repository provides:
 * - Standard CRUD operations through JpaRepository
 * - Custom query methods for genre searches
 * - Pagination support for large datasets
 * - Performance-optimized queries
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    /**
     * Find genre by name (case-insensitive exact match)
     */
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Find genres by name (case-insensitive partial match)
     */
    Page<Genre> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Find genres by category
     */
    Page<Genre> findByCategoryIgnoreCase(String category, Pageable pageable);

    /**
     * Find active genres
     */
    Page<Genre> findByIsActiveTrue(Pageable pageable);

    /**
     * Find genres by category and active status
     */
    Page<Genre> findByCategoryIgnoreCaseAndIsActiveTrue(String category, Pageable pageable);

    /**
     * Find genres with books in a specific price range
     */
    @Query("SELECT DISTINCT g FROM Genre g " +
           "JOIN g.books b " +
           "WHERE b.price BETWEEN :minPrice AND :maxPrice " +
           "AND g.isActive = true")
    List<Genre> findGenresByBookPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    /**
     * Find genres with books published in a specific year
     */
    @Query("SELECT DISTINCT g FROM Genre g " +
           "JOIN g.books b " +
           "WHERE b.publishedYear = :year " +
           "AND g.isActive = true")
    List<Genre> findGenresByPublishedYear(@Param("year") Integer year);

    /**
     * Count books by genre
     */
    @Query("SELECT COUNT(b) FROM Book b JOIN b.genres g WHERE g.id = :genreId AND b.isActive = true")
    long countBooksByGenre(@Param("genreId") Long genreId);

    /**
     * Find genres with most books (top N)
     */
    @Query("SELECT g, COUNT(b) as bookCount FROM Genre g " +
           "JOIN g.books b " +
           "WHERE g.isActive = true AND b.isActive = true " +
           "GROUP BY g " +
           "ORDER BY bookCount DESC")
    Page<Object[]> findGenresByBookCount(Pageable pageable);

    /**
     * Find genres by multiple names
     */
    List<Genre> findByNameIn(List<String> names);

    /**
     * Find genres by multiple categories
     */
    List<Genre> findByCategoryIn(List<String> categories);
}
