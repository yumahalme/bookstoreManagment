package com.bookstore.service;

import com.bookstore.domain.Author;
import com.bookstore.domain.Book;
import com.bookstore.domain.Genre;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.dto.GenreDto;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.GenreRepository;
import com.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService implementation.
 * 
 * This test class demonstrates:
 * - Mock-based testing approach
 * - Business logic validation
 * - Error scenario handling
 * - Service layer testing best practices
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto testBookDto;
    private Book testBook;
    private Author testAuthor;
    private Genre testGenre;

    @BeforeEach
    void setUp() {
        // Setup test data
        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setName("Test Author");
        testAuthor.setEmail("author@test.com");

        testGenre = new Genre();
        testGenre.setId(1L);
        testGenre.setName("Test Genre");
        testGenre.setCategory("Fiction");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setIsbn("1234567890123");
        testBook.setPrice(new BigDecimal("29.99"));
        testBook.setStockQuantity(10);
        testBook.setPublishedYear(2023);
        testBook.setPublisher("Test Publisher");
        testBook.setLanguage("English");
        testBook.setPageCount(300);
        testBook.setIsActive(true);

        Set<Author> authors = new HashSet<>();
        authors.add(testAuthor);
        testBook.setAuthors(authors);

        Set<Genre> genres = new HashSet<>();
        genres.add(testGenre);
        testBook.setGenres(genres);

        // Setup DTO
        testBookDto = new BookDto();
        testBookDto.setTitle("Test Book");
        testBookDto.setIsbn("1234567890123");
        testBookDto.setPrice(new BigDecimal("29.99"));
        testBookDto.setStockQuantity(10);
        testBookDto.setPublishedYear(2023);
        testBookDto.setPublisher("Test Publisher");
        testBookDto.setLanguage("English");
        testBookDto.setPageCount(300);

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("Test Author");
        authorDto.setEmail("author@test.com");

        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Test Genre");
        genreDto.setCategory("Fiction");

        Set<AuthorDto> authorDtos = new HashSet<>();
        authorDtos.add(authorDto);
        testBookDto.setAuthors(authorDtos);

        Set<GenreDto> genreDtos = new HashSet<>();
        genreDtos.add(genreDto);
        testBookDto.setGenres(genreDtos);
    }

    @Test
    void createBook_Success() {
        // Given
        when(bookRepository.existsByIsbnAndIsActiveTrue(anyString())).thenReturn(false);
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(testAuthor));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(testGenre));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        BookDto result = bookService.createBook(testBookDto);

        // Then
        assertNotNull(result);
        assertEquals(testBookDto.getTitle(), result.getTitle());
        assertEquals(testBookDto.getIsbn(), result.getIsbn());
        assertEquals(testBookDto.getPrice(), result.getPrice());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_DuplicateIsbn_ThrowsException() {
        // Given
        when(bookRepository.existsByIsbnAndIsActiveTrue(anyString())).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(testBookDto);
        });

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getBookById_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // When
        Optional<BookDto> result = bookService.getBookById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testBook.getTitle(), result.get().getTitle());
        assertEquals(testBook.getIsbn(), result.get().getIsbn());
    }

    @Test
    void getBookById_NotFound_ReturnsEmpty() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<BookDto> result = bookService.getBookById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getBookById_InactiveBook_ReturnsEmpty() {
        // Given
        testBook.setIsActive(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // When
        Optional<BookDto> result = bookService.getBookById(1L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void updateBook_Success() {
        // Given
        BookDto updateDto = new BookDto();
        updateDto.setTitle("Updated Book Title");
        updateDto.setPrice(new BigDecimal("39.99"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.existsByIsbnAndIsActiveTrue(anyString())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        BookDto result = bookService.updateBook(1L, updateDto);

        // Then
        assertNotNull(result);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_BookNotFound_ThrowsException() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(999L, testBookDto);
        });

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_DuplicateIsbn_ThrowsException() {
        // Given
        BookDto updateDto = new BookDto();
        updateDto.setIsbn("9876543210987");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.existsByIsbnAndIsActiveTrue("9876543210987")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(1L, updateDto);
        });

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository).save(any(Book.class));
        assertFalse(testBook.getIsActive());
    }

    @Test
    void deleteBook_BookNotFound_ThrowsException() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBook(999L);
        });

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void restoreBook_Success() {
        // Given
        testBook.setIsActive(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        BookDto result = bookService.restoreBook(1L);

        // Then
        assertNotNull(result);
        assertTrue(testBook.getIsActive());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateStockQuantity_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        BookDto result = bookService.updateStockQuantity(1L, 25);

        // Then
        assertNotNull(result);
        assertEquals(25, testBook.getStockQuantity());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateStockQuantity_NegativeQuantity_ThrowsException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateStockQuantity(1L, -5);
        });

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void existsByIsbn_ReturnsTrue() {
        // Given
        when(bookRepository.existsByIsbnAndIsActiveTrue("1234567890123")).thenReturn(true);

        // When
        boolean result = bookService.existsByIsbn("1234567890123");

        // Then
        assertTrue(result);
    }

    @Test
    void existsByIsbn_ReturnsFalse() {
        // Given
        when(bookRepository.existsByIsbnAndIsActiveTrue("1234567890123")).thenReturn(false);

        // When
        boolean result = bookService.existsByIsbn("1234567890123");

        // Then
        assertFalse(result);
    }
}
