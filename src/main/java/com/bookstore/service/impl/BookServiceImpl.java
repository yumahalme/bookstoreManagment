package com.bookstore.service.impl;

import com.bookstore.domain.Author;
import com.bookstore.domain.Book;
import com.bookstore.domain.Genre;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.dto.BookSearchDto;
import com.bookstore.dto.GenreDto;
import com.bookstore.dto.PageResponseDto;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.GenreRepository;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of BookService interface.
 * 
 * This service provides:
 * - Business logic for book management
 * - CRUD operations for books
 * - Search and filtering capabilities
 * - Inventory management
 * - Data validation and business rules
 * 
 * Design decisions:
 * - Transactional operations for data consistency
 * - Proper error handling and validation
 * - Efficient data transformation between DTOs and entities
 * - Business rule enforcement
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, 
                         AuthorRepository authorRepository, 
                         GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        // Validate ISBN uniqueness
        if (bookDto.getIsbn() != null && existsByIsbn(bookDto.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + bookDto.getIsbn() + " already exists");
        }

        Book book = convertToEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));

        // Validate ISBN uniqueness if changed
        if (bookDto.getIsbn() != null && !bookDto.getIsbn().equals(existingBook.getIsbn())) {
            if (existsByIsbn(bookDto.getIsbn())) {
                throw new IllegalArgumentException("Book with ISBN " + bookDto.getIsbn() + " already exists");
            }
        }

        updateBookFromDto(existingBook, bookDto);
        Book updatedBook = bookRepository.save(existingBook);
        return convertToDto(updatedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .filter(Book::getIsActive)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbnAndIsActiveTrue(isbn)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByIsActiveTrue(pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> searchBooks(BookSearchDto searchDto) {
        // Validate search criteria
        if (!searchDto.hasSearchCriteria()) {
            return getAllBooks(searchDto.getPage(), searchDto.getSize());
        }

        if (!searchDto.isValidPriceRange()) {
            throw new IllegalArgumentException("Invalid price range: min price cannot be greater than max price");
        }

        if (!searchDto.isValidYearRange()) {
            throw new IllegalArgumentException("Invalid year range: min year cannot be greater than max year");
        }

        // Create pageable with sorting
        Sort sort = Sort.by(Sort.Direction.fromString(searchDto.getSortDirection()), searchDto.getSortBy());
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        // Convert search parameters
        BigDecimal minPrice = searchDto.getMinPrice() != null ? BigDecimal.valueOf(searchDto.getMinPrice()) : null;
        BigDecimal maxPrice = searchDto.getMaxPrice() != null ? BigDecimal.valueOf(searchDto.getMaxPrice()) : null;

        // Perform search
        Page<Book> bookPage = bookRepository.findBySearchCriteria(
                searchDto.getTitle(),
                searchDto.getAuthor(),
                searchDto.getGenre(),
                searchDto.getPublisher(),
                searchDto.getLanguage(),
                searchDto.getMinYear(),
                searchDto.getMaxYear(),
                minPrice,
                maxPrice,
                searchDto.getInStock(),
                pageable
        );

        return convertToPageResponse(bookPage);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        book.setIsActive(false);
        bookRepository.save(book);
    }

    @Override
    public BookDto restoreBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        book.setIsActive(true);
        Book restoredBook = bookRepository.save(book);
        return convertToDto(restoredBook);
    }

    @Override
    public BookDto updateStockQuantity(Long id, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
        book.setStockQuantity(newQuantity);
        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getBooksWithLowStock(int threshold) {
        List<Book> books = bookRepository.findBooksWithLowStock(threshold);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksByGenre(String genreName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByGenreNameContainingIgnoreCase(genreName, pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksByAuthor(String authorName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByAuthorNameContainingIgnoreCase(authorName, pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksByPublisher(String publisher, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByPublisherContainingIgnoreCaseAndIsActiveTrue(publisher, pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksByPriceRange(Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        BigDecimal min = minPrice != null ? BigDecimal.valueOf(minPrice) : BigDecimal.ZERO;
        BigDecimal max = maxPrice != null ? BigDecimal.valueOf(maxPrice) : BigDecimal.valueOf(Double.MAX_VALUE);
        Page<Book> bookPage = bookRepository.findByPriceBetweenAndIsActiveTrue(min, max, pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksByYearRange(Integer startYear, Integer endYear, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedYear").descending());
        Page<Book> bookPage = bookRepository.findByPublishedYearBetweenAndIsActiveTrue(startYear, endYear, pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksInStock(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByStockQuantityGreaterThanAndIsActiveTrue(0, pageable);
        return convertToPageResponse(bookPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BookDto> getBooksOutOfStock(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> bookPage = bookRepository.findByStockQuantityGreaterThanAndIsActiveTrue(0, pageable);
        // Filter out books in stock
        List<Book> outOfStockBooks = bookPage.getContent().stream()
                .filter(book -> book.getStockQuantity() == 0)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(outOfStockBooks.stream().map(this::convertToDto).collect(Collectors.toList()),
                page, size, bookPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public BookStatistics getBookStatistics() {
        long totalBooks = bookRepository.count();
        long booksInStock = bookRepository.findByStockQuantityGreaterThanAndIsActiveTrue(0, Pageable.unpaged()).getTotalElements();
        long booksOutOfStock = totalBooks - booksInStock;
        
        // Calculate average price and total inventory value
        List<Book> allBooks = bookRepository.findByIsActiveTrue(Pageable.unpaged()).getContent();
        double averagePrice = allBooks.stream()
                .mapToDouble(book -> book.getPrice().doubleValue())
                .average()
                .orElse(0.0);
        
        double totalInventoryValue = allBooks.stream()
                .mapToDouble(book -> book.getPrice().doubleValue() * book.getStockQuantity())
                .sum();

        return new BookStatistics(totalBooks, booksInStock, booksOutOfStock, 
                                genreRepository.count(), authorRepository.count(), 
                                averagePrice, totalInventoryValue);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbnAndIsActiveTrue(isbn);
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookCountByGenre(String genreName) {
        return bookRepository.countByGenre(genreName);
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookCountByAuthor(String authorName) {
        return bookRepository.countByAuthor(authorName);
    }

    // Helper methods for entity-DTO conversion
    private Book convertToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setDescription(bookDto.getDescription());
        book.setPrice(bookDto.getPrice());
        book.setStockQuantity(bookDto.getStockQuantity());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setPublisher(bookDto.getPublisher());
        book.setLanguage(bookDto.getLanguage() != null ? bookDto.getLanguage() : "English");
        book.setPageCount(bookDto.getPageCount());
        book.setIsActive(bookDto.getIsActive() != null ? bookDto.getIsActive() : true);

        // Handle authors
        if (bookDto.getAuthors() != null) {
            Set<Author> authors = bookDto.getAuthors().stream()
                    .map(this::convertAuthorDtoToEntity)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        // Handle genres
        if (bookDto.getGenres() != null) {
            Set<Genre> genres = bookDto.getGenres().stream()
                    .map(this::convertGenreDtoToEntity)
                    .collect(Collectors.toSet());
            book.setGenres(genres);
        }

        return book;
    }

    private void updateBookFromDto(Book book, BookDto bookDto) {
        if (bookDto.getTitle() != null) book.setTitle(bookDto.getTitle());
        if (bookDto.getIsbn() != null) book.setIsbn(bookDto.getIsbn());
        if (bookDto.getDescription() != null) book.setDescription(bookDto.getDescription());
        if (bookDto.getPrice() != null) book.setPrice(bookDto.getPrice());
        if (bookDto.getStockQuantity() != null) book.setStockQuantity(bookDto.getStockQuantity());
        if (bookDto.getPublishedYear() != null) book.setPublishedYear(bookDto.getPublishedYear());
        if (bookDto.getPublisher() != null) book.setPublisher(bookDto.getPublisher());
        if (bookDto.getLanguage() != null) book.setLanguage(bookDto.getLanguage());
        if (bookDto.getPageCount() != null) book.setPageCount(bookDto.getPageCount());
        if (bookDto.getIsActive() != null) book.setIsActive(bookDto.getIsActive());

        // Handle authors
        if (bookDto.getAuthors() != null) {
            Set<Author> authors = bookDto.getAuthors().stream()
                    .map(this::convertAuthorDtoToEntity)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        // Handle genres
        if (bookDto.getGenres() != null) {
            Set<Genre> genres = bookDto.getGenres().stream()
                    .map(this::convertGenreDtoToEntity)
                    .collect(Collectors.toSet());
            book.setGenres(genres);
        }
    }

    private BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());
        bookDto.setStockQuantity(book.getStockQuantity());
        bookDto.setPublishedYear(book.getPublishedYear());
        bookDto.setPublisher(book.getPublisher());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setPageCount(book.getPageCount());
        bookDto.setIsActive(book.getIsActive());
        bookDto.setCreatedAt(book.getCreatedAt() != null ? book.getCreatedAt().toString() : null);
        bookDto.setUpdatedAt(book.getUpdatedAt() != null ? book.getUpdatedAt().toString() : null);

        // Convert authors
        if (book.getAuthors() != null) {
            Set<AuthorDto> authors = book.getAuthors().stream()
                    .map(this::convertAuthorToDto)
                    .collect(Collectors.toSet());
            bookDto.setAuthors(authors);
        }

        // Convert genres
        if (book.getGenres() != null) {
            Set<GenreDto> genres = book.getGenres().stream()
                    .map(this::convertGenreToDto)
                    .collect(Collectors.toSet());
            bookDto.setGenres(genres);
        }

        return bookDto;
    }

    private Author convertAuthorDtoToEntity(AuthorDto authorDto) {
        if (authorDto.getId() != null) {
            return authorRepository.findById(authorDto.getId())
                    .orElseGet(() -> createNewAuthor(authorDto));
        } else {
            return createNewAuthor(authorDto);
        }
    }

    private Author createNewAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());
        author.setBiography(authorDto.getBiography());
        author.setEmail(authorDto.getEmail());
        author.setCountry(authorDto.getCountry());
        author.setBirthYear(authorDto.getBirthYear());
        return author;
    }

    private Genre convertGenreDtoToEntity(GenreDto genreDto) {
        if (genreDto.getId() != null) {
            return genreRepository.findById(genreDto.getId())
                    .orElseGet(() -> createNewGenre(genreDto));
        } else {
            return createNewGenre(genreDto);
        }
    }

    private Genre createNewGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setName(genreDto.getName());
        genre.setDescription(genreDto.getDescription());
        genre.setCategory(genreDto.getCategory());
        genre.setIsActive(genreDto.getIsActive() != null ? genreDto.getIsActive() : true);
        return genre;
    }

    private AuthorDto convertAuthorToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setBiography(author.getBiography());
        authorDto.setEmail(author.getEmail());
        authorDto.setCountry(author.getCountry());
        authorDto.setBirthYear(author.getBirthYear());
        return authorDto;
    }

    private GenreDto convertGenreToDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        genreDto.setDescription(genre.getDescription());
        genreDto.setCategory(genre.getCategory());
        genreDto.setIsActive(genre.getIsActive());
        return genreDto;
    }

    private PageResponseDto<BookDto> convertToPageResponse(Page<Book> bookPage) {
        List<BookDto> bookDtos = bookPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new PageResponseDto<>(bookDtos, bookPage.getNumber(), bookPage.getSize(), bookPage.getTotalElements());
    }
}
