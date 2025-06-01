package com.example.Bookstore.controller;

import com.example.Bookstore.exception.BookNotFoundException;
import com.example.Bookstore.model.Book;
import com.example.Bookstore.payload.BookDTO;
import com.example.Bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "USER")
    public void getBookById_whenExists_returns200() throws Exception {
        final var book = new Book();
        book.setId(1L);
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");
        book.setPrice(new BigDecimal("50.0"));
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getBookById_whenNotFound_returns404() throws Exception {
        when(bookService.getBookById(2L)).thenThrow(new BookNotFoundException(2L));

        mockMvc.perform(get("/api/v1/books/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id: 2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getBooks_whenExists_return200() throws Exception {
        final var book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Refactoring");
        book1.setAuthor("Martin Fowler");
        book1.setPrice(new BigDecimal("50.0"));

        final var book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Harry Potter");
        book2.setAuthor("J.K. Rowling");
        book2.setPrice(new BigDecimal("499.99"));

        when(bookService.getBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Refactoring"))
                .andExpect(jsonPath("$[1].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getBooks_whenNotFound_returns404() throws Exception {
        when(bookService.getBooks()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBooks_whenExists_return200() throws Exception {
        final var book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Refactoring");
        book1.setAuthor("Martin Fowler");
        book1.setPrice(new BigDecimal("50.0"));

        final var book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Harry Potter");
        book2.setAuthor("J.K. Rowling");
        book2.setPrice(new BigDecimal("499.99"));

        when(bookService.deleteBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(delete("/api/v1/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Refactoring"))
                .andExpect(jsonPath("$[1].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBooks_whenNotFound_returns404() throws Exception {
        when(bookService.deleteBooks()).thenReturn(List.of());

        mockMvc.perform(delete("/api/v1/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBookById_whenExists_returns200() throws Exception {
        final var book = new Book();
        book.setId(1L);
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");
        book.setPrice(new BigDecimal("50.0"));
        when(bookService.getBookById(1L)).thenReturn(book);
        when(bookService.deleteBookById(1L)).thenReturn(book);

        mockMvc.perform(delete("/api/v1/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBookById_whenNotFound_returns404() throws Exception {
        when(bookService.deleteBookById(2L)).thenThrow(new BookNotFoundException(2L));

        mockMvc.perform(delete("/api/v1/books/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id: 2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addBook_whenAdded_returns201() throws Exception {
        final var bookDTO = new BookDTO("Refactoring", "Martin Fowler", new BigDecimal("50.0"));
        final var book = new Book();
        book.setId(1L);
        book.setTitle(bookDTO.title());
        book.setAuthor(bookDTO.author());
        book.setPrice(bookDTO.price());

        when(bookService.addBook(bookDTO)).thenReturn(book);

        mockMvc.perform(post("/api/v1/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addListOfBooks_whenAdded_returns201() throws Exception {
        final var bookDTO1 = new BookDTO("Refactoring", "Martin Fowler", new BigDecimal("50.0"));
        final var book1 = new Book();
        book1.setId(1L);
        book1.setTitle(bookDTO1.title());
        book1.setAuthor(bookDTO1.author());
        book1.setPrice(bookDTO1.price());

        final var bookDTO2 = new BookDTO("Harry Potter", "J.K. Rowling", new BigDecimal("499.99"));
        final var book2 = new Book();
        book2.setId(2L);
        book2.setTitle(bookDTO2.title());
        book2.setAuthor(bookDTO2.author());
        book2.setPrice(bookDTO2.price());

        final var listOfBookDTOs = List.of(bookDTO1, bookDTO2);
        final var listOfBooks = List.of(book1, book2);

        when(bookService.addBooks(listOfBookDTOs)).thenReturn(listOfBooks);

        mockMvc.perform(post("/api/v1/books/all")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(listOfBookDTOs)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Refactoring"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateBook_whenAdded_returns200() throws Exception {
        final var bookDTO = new BookDTO("Refactoring", "Martin Fowler", new BigDecimal("50.0"));
        final var storedBook = new Book();
        storedBook.setId(1L);
        storedBook.setTitle("Harry Potter");
        storedBook.setAuthor("J.K. Rowling");
        storedBook.setPrice(new BigDecimal("499.99"));
        final var updatedStoredBook = new Book();
        updatedStoredBook.setId(1L);
        updatedStoredBook.setTitle(bookDTO.title());
        updatedStoredBook.setAuthor(bookDTO.author());
        updatedStoredBook.setPrice(bookDTO.price());

        when(bookService.getBookById(1L)).thenReturn(storedBook);
        when(bookService.updateBook(1L, bookDTO)).thenReturn(updatedStoredBook);

        mockMvc.perform(put("/api/v1/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateBook_whenNotFound_returns404() throws Exception {
        final var bookDTO = new BookDTO("Refactoring", "Martin Fowler", new BigDecimal("50.0"));

        when(bookService.getBookById(2L)).thenThrow(new BookNotFoundException(2L));
        when(bookService.updateBook(2L, bookDTO)).thenThrow(new BookNotFoundException(2L));

        mockMvc.perform(put("/api/v1/books/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id: 2"));
    }
}
