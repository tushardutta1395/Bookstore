package com.example.Bookstore.controller;

import com.example.Bookstore.exception.BookNotFoundException;
import com.example.Bookstore.model.Book;
import com.example.Bookstore.service.BookService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        final var book = Book.builder()
                .id(1L)
                .title("Refactoring")
                .author("Martin Fowler")
                .price(new BigDecimal("50.0"))
                .build();
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
        final var book1 = Book.builder()
                .id(1L)
                .title("Refactoring")
                .author("Martin Fowler")
                .price(new BigDecimal("50.0"))
                .build();

        final var book2 = Book.builder()
                .id(2L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(new BigDecimal("499.99"))
                .build();

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
        final var book1 = Book.builder()
                .id(1L)
                .title("Refactoring")
                .author("Martin Fowler")
                .price(new BigDecimal("50.0"))
                .build();

        final var book2 = Book.builder()
                .id(2L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(new BigDecimal("499.99"))
                .build();

        when(bookService.deleteBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(delete("/api/v1/books")
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
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBookById_whenExists_returns200() throws Exception {
        final var book = Book.builder()
                .id(1L)
                .title("Refactoring")
                .author("Martin Fowler")
                .price(new BigDecimal("50.0"))
                .build();
        when(bookService.deleteBookById(1L)).thenReturn(book);

        mockMvc.perform(delete("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBookById_whenNotFound_returns404() throws Exception {
        when(bookService.deleteBookById(2L)).thenThrow(new BookNotFoundException(2L));

        mockMvc.perform(delete("/api/v1/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id: 2"));
    }
}
