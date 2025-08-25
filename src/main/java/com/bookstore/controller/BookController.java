package com.bookstore.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.dto.BookSearchDto;
import com.bookstore.dto.PageResponseDto;
import com.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book-related operations.
 * 
 * This controller provides:
 * - CRUD operations for books
 * - Search and filtering endpoints
 * - Pagination support
 * - Role-based access control
 * - Comprehensive API documentation
 * 
 * Design decisions:
 * - RESTful API design principles
 * - Proper HTTP status codes
 * - Input validation and error handling
 * - Security annotations for access control
 * - Swagger documentation for API clarity
 */
@RestController
@RequestMapping("/books")
@Tag(name = "Book Management", description = "APIs for managing bookstore inventory")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book", description = "Creates a new book in the inventory. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = BookDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "409", description = "Book with ISBN already exists")
    })
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing book", description = "Updates an existing book in the inventory. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "409", description = "Book with ISBN already exists")
    })
    public ResponseEntity<BookDto> updateBook(
            @Parameter(description = "Book ID") @PathVariable Long id,
            @Valid @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDto> getBookById(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get book by ISBN", description = "Retrieves a book by its ISBN. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDto> getBookByIsbn(
            @Parameter(description = "Book ISBN") @PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get all books", description = "Retrieves all books with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getAllBooks(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Search books", description = "Searches books by various criteria with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> searchBooks(@Valid @RequestBody BookSearchDto searchDto) {
        PageResponseDto<BookDto> searchResults = bookService.searchBooks(searchDto);
        return ResponseEntity.ok(searchResults);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book", description = "Soft deletes a book from the inventory. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore a deleted book", description = "Restores a soft-deleted book. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book restored successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDto> restoreBook(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        BookDto restoredBook = bookService.restoreBook(id);
        return ResponseEntity.ok(restoredBook);
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book stock", description = "Updates the stock quantity of a book. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid stock quantity"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDto> updateStock(
            @Parameter(description = "Book ID") @PathVariable Long id,
            @Parameter(description = "New stock quantity") @RequestParam Integer quantity) {
        BookDto updatedBook = bookService.updateStockQuantity(id, quantity);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get books with low stock", description = "Retrieves books with stock below threshold. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<List<BookDto>> getBooksWithLowStock(
            @Parameter(description = "Stock threshold") @RequestParam(defaultValue = "5") int threshold) {
        List<BookDto> books = bookService.getBooksWithLowStock(threshold);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/genre/{genreName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books by genre", description = "Retrieves books by genre with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksByGenre(
            @Parameter(description = "Genre name") @PathVariable String genreName,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksByGenre(genreName, page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/author/{authorName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books by author", description = "Retrieves books by author with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksByAuthor(
            @Parameter(description = "Author name") @PathVariable String authorName,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksByAuthor(authorName, page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/publisher/{publisher}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books by publisher", description = "Retrieves books by publisher with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksByPublisher(
            @Parameter(description = "Publisher name") @PathVariable String publisher,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksByPublisher(publisher, page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/price-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books by price range", description = "Retrieves books within a price range with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid price range"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksByPriceRange(
            @Parameter(description = "Minimum price") @RequestParam Double minPrice,
            @Parameter(description = "Maximum price") @RequestParam Double maxPrice,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksByPriceRange(minPrice, maxPrice, page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/year-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books by year range", description = "Retrieves books published within a year range with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid year range"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksByYearRange(
            @Parameter(description = "Start year") @RequestParam Integer startYear,
            @Parameter(description = "End year") @RequestParam Integer endYear,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksByYearRange(startYear, endYear, page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books in stock", description = "Retrieves books currently in stock with pagination. Admin and User access allowed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksInStock(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksInStock(page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get books out of stock", description = "Retrieves books currently out of stock with pagination. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<PageResponseDto<BookDto>> getBooksOutOfStock(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponseDto<BookDto> books = bookService.getBooksOutOfStock(page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get book statistics", description = "Retrieves comprehensive statistics about the book inventory. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<BookService.BookStatistics> getBookStatistics() {
        BookService.BookStatistics statistics = bookService.getBookStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/check-isbn/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Check ISBN availability", description = "Checks if an ISBN is already in use. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ISBN availability checked successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    })
    public ResponseEntity<Boolean> checkIsbnAvailability(
            @Parameter(description = "ISBN to check") @PathVariable String isbn) {
        boolean exists = bookService.existsByIsbn(isbn);
        return ResponseEntity.ok(exists);
    }
}
