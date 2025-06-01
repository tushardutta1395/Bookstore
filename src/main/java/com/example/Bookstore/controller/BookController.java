package com.example.Bookstore.controller;

import com.example.Bookstore.payload.BookDTO;
import com.example.Bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Book Controller", description = "Controller for book management")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/books", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Fetched List of Books")
    @ApiResponse(responseCode = "404", description = "List of Books not found")
    @ApiResponse(responseCode = "400", description = "List of Books Error")
    @ApiResponse(responseCode = "500", description = "List of Books Internal Server Error")
    @Operation(summary = "Get List of Books")
    public ResponseEntity<?> getBooks() {
        final var books = bookService.getBooks();
        return !books.isEmpty()
                ? ResponseEntity.ok(books)
                : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/books/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Fetched Single Book")
    @ApiResponse(responseCode = "404", description = "Single Book not found")
    @ApiResponse(responseCode = "400", description = "Single Book Error")
    @ApiResponse(responseCode = "500", description = "Single Book Internal Server Error")
    @Operation(summary = "Get Single Book")
    public ResponseEntity<?> getBookById(@PathVariable final Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @DeleteMapping(value = "/books", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Deleted List of Books")
    @ApiResponse(responseCode = "404", description = "Deleted List of Books not found")
    @ApiResponse(responseCode = "400", description = "Deleted List of Books Error")
    @ApiResponse(responseCode = "500", description = "Deleted List of Books Internal Server Error")
    @Operation(summary = "Delete List of Books")
    public ResponseEntity<?> deleteBooks() {
        final var books = bookService.deleteBooks();
        return !books.isEmpty()
                ? ResponseEntity.ok(books)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/books/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Deleted Single Book")
    @ApiResponse(responseCode = "404", description = "Deleted Single Book not found")
    @ApiResponse(responseCode = "400", description = "Deleted Single Book Error")
    @ApiResponse(responseCode = "500", description = "Deleted Single Book Internal Server Error")
    @Operation(summary = "Delete Single Book")
    public ResponseEntity<?> deleteBookById(@PathVariable final Long id) {
        return ResponseEntity.ok(bookService.deleteBookById(id));
    }

    @PostMapping(value = "/books", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Created Book")
    @ApiResponse(responseCode = "404", description = "Created Book not found")
    @ApiResponse(responseCode = "400", description = "Created Book Error")
    @ApiResponse(responseCode = "403", description = "Created Book Invalid")
    @ApiResponse(responseCode = "500", description = "Created Book Internal Server Error")
    @Operation(summary = "Create Book")
    public ResponseEntity<?> addBook(@Valid @RequestBody final BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(bookDTO));
    }

    @PostMapping(value = "/books/all", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Created List of Books")
    @ApiResponse(responseCode = "404", description = "Created List of Books not found")
    @ApiResponse(responseCode = "400", description = "Created List of Books Error")
    @ApiResponse(responseCode = "403", description = "Created List of Books Invalid")
    @ApiResponse(responseCode = "500", description = "Created List of Books Internal Server Error")
    @Operation(summary = "Create List of Books")
    public ResponseEntity<?> addBooks(@Valid @RequestBody final List<BookDTO> booksDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBooks(booksDTO));
    }

    @PutMapping(value = "/books/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Updated Book")
    @ApiResponse(responseCode = "404", description = "Updated Book not found")
    @ApiResponse(responseCode = "400", description = "Updated Book Error")
    @ApiResponse(responseCode = "403", description = "Updated Book Invalid")
    @ApiResponse(responseCode = "500", description = "Updated Book Internal Server Error")
    @Operation(summary = "Update Book")
    public ResponseEntity<?> updateBook(@PathVariable final Long id, @Valid @RequestBody final BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }
}
