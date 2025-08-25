package com.bookstore.repository;

import com.bookstore.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Author entities.
 * 
 * This repository provides:
 * - Standard CRUD operations through JpaRepository
 * - Custom query methods for author searches
 * - Pagination support for large datasets
 * - Performance-optimized queries
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find author by name (case-insensitive exact match)
     */
    Optional<Author> findByNameIgnoreCase(String name);

    /**
     * Find authors by name (case-insensitive partial match)
     */
    Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Find author by email
     */
    Optional<Author> findByEmail(String email);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find authors by country
     */
    Page<Author> findByCountryIgnoreCase(String country, Pageable pageable);

    /**
     * Find authors by birth year
     */
    List<Author> findByBirthYear(Integer birthYear);

    /**
     * Find authors by birth year range
     */
    List<Author> findByBirthYearBetween(Integer startYear, Integer endYear);

    /**
     * Find authors with books in a specific genre
     */
    @Query("SELECT DISTINCT a FROM Author a " +
           "JOIN a.books b " +
           "JOIN b.genres g " +
           "WHERE g.name = :genreName")
    List<Author> findAuthorsByGenre(@Param("genreName") String genreName);

    /**
     * Find authors with books published in a specific year
     */
    @Query("SELECT DISTINCT a FROM Author a " +
           "JOIN a.books b " +
           "WHERE b.publishedYear = :year")
    List<Author> findAuthorsByPublishedYear(@Param("year") Integer year);

    /**
     * Find authors with books in a specific price range
     */
    @Query("SELECT DISTINCT a FROM Author a " +
           "JOIN a.books b " +
           "WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Author> findAuthorsByBookPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    /**
     * Count books by author
     */
    @Query("SELECT COUNT(b) FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    long countBooksByAuthor(@Param("authorId") Long authorId);

    /**
     * Find authors with most books (top N)
     */
    @Query("SELECT a, COUNT(b) as bookCount FROM Author a " +
           "JOIN a.books b " +
           "GROUP BY a " +
           "ORDER BY bookCount DESC")
    Page<Object[]> findAuthorsByBookCount(Pageable pageable);
}
