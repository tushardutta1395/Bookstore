package com.example.Bookstore.service;

import com.example.Bookstore.exception.BookNotFoundException;
import com.example.Bookstore.model.Book;
import com.example.Bookstore.payload.BookDTO;
import com.example.Bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(final Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> deleteBooks() {
        final var books = getBooks();
        if (!books.isEmpty()) {
            bookRepository.deleteAll();
        }
        return books;
    }

    public Book deleteBookById(final Long id) {
        final var book = getBookById(id);
        bookRepository.deleteById(id);
        return book;
    }

    public Book addBook(final BookDTO bookDTO) {
        return bookRepository.save(mapBookDTOToBook(bookDTO));
    }

    public List<Book> addBooks(final List<BookDTO> booksDTO) {
        if (booksDTO != null) {
            return bookRepository.saveAll(booksDTO.stream().map(this::mapBookDTOToBook).toList());
        }
        return null;
    }

    public List<Book> addBooks(final BookDTO... booksDTO) {
        return bookRepository.saveAll(Arrays.stream(booksDTO).map(this::mapBookDTOToBook).toList());
    }

    public Book updateBook(final Long id, final BookDTO bookDTO) {
        if (bookDTO != null) {
            final var updatedBook = getBookById(id);
            updatedBook.setTitle(bookDTO.title());
            updatedBook.setAuthor(bookDTO.author());
            updatedBook.setPrice(bookDTO.price());
            return bookRepository.save(updatedBook);
        }
        return null;
    }

    private Book mapBookDTOToBook(final BookDTO bookDTO) {
        return bookDTO != null
                ? Book.builder()
                .title(bookDTO.title())
                .author(bookDTO.author())
                .price(bookDTO.price())
                .build()
                : null;
    }
}
