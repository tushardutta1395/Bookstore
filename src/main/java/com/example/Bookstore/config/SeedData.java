package com.example.Bookstore.config;

import com.example.Bookstore.payload.BookDTO;
import com.example.Bookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class SeedData implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    @Override
    public void run(final String... args) {
        final var bookDTO1 = new BookDTO("Harry Potter", "J.K. Rowling", new BigDecimal("599.99"));
        final var bookDTO2 = new BookDTO("Pirates of the Caribbean", "Jonny Depp", new BigDecimal("299.99"));
        final var books = bookService.addBooks(bookDTO1, bookDTO2);
        log.info(!books.isEmpty()
                ? "Books added: " + books
                : "Books cannot be added: " + books
        );
    }
}
