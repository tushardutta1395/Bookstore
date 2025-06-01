package com.example.Bookstore.service;

import com.example.Bookstore.exception.BookNotFoundException;
import com.example.Bookstore.model.Book;
import com.example.Bookstore.payload.BookDTO;
import com.example.Bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getBooks_whenBooksExist_returnBooks() {
        final var mockBook1 = new Book();
        mockBook1.setId(1L);
        mockBook1.setTitle("Clean Code");
        mockBook1.setAuthor("Robert C. Martin");
        mockBook1.setPrice(new BigDecimal("40.0"));
        final var mockBook2 = new Book();
        mockBook2.setId(2L);
        mockBook2.setTitle("Harry Potter");
        mockBook2.setAuthor("J.K. Rowling");
        mockBook2.setPrice(new BigDecimal("599.99"));
        when(bookRepository.findAll()).thenReturn(List.of(mockBook1, mockBook2));
        final var found = bookService.getBooks();

        assertEquals("Clean Code", found.get(0).getTitle());
        assertEquals("Harry Potter", found.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBooks_whenBooksNotFound_throwsException() {
        when(bookRepository.findAll()).thenReturn(List.of());
        assertEquals(List.of(), bookService.getBooks());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookById_whenBookExists_returnsBook() {
        final var mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Clean Code");
        mockBook.setAuthor("Robert C. Martin");
        mockBook.setPrice(new BigDecimal("40.0"));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        final var found = bookService.getBookById(1L);

        assertEquals("Clean Code", found.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void getBookById_whenBookNotFound_throwsException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2L));
        verify(bookRepository, times(1)).findById(2L);
    }

    @Test
    public void deleteBooks_whenBooksExist_returnBooks() {
        final var mockBook1 = new Book();
        mockBook1.setId(1L);
        mockBook1.setTitle("Clean Code");
        mockBook1.setAuthor("Robert C. Martin");
        mockBook1.setPrice(new BigDecimal("40.0"));
        final var mockBook2 = new Book();
        mockBook2.setId(2L);
        mockBook2.setTitle("Harry Potter");
        mockBook2.setAuthor("J.K. Rowling");
        mockBook2.setPrice(new BigDecimal("599.99"));
        when(bookRepository.findAll()).thenReturn(List.of(mockBook1, mockBook2));
        final var found = bookService.deleteBooks();

        assertEquals("Clean Code", found.get(0).getTitle());
        assertEquals("Harry Potter", found.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
        verify(bookRepository, times(1)).deleteAll();
    }

    @Test
    public void deleteBooks_whenBooksNotFound_throwsException() {
        when(bookRepository.findAll()).thenReturn(List.of());
        assertEquals(List.of(), bookService.deleteBooks());
        verify(bookRepository, times(1)).findAll();
        verify(bookRepository, times(0)).deleteAll();
    }

    @Test
    public void deleteBookById_whenBookExists_returnsBook() {
        final var mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Clean Code");
        mockBook.setAuthor("Robert C. Martin");
        mockBook.setPrice(new BigDecimal("40.0"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        final var found = bookService.deleteBookById(1L);

        assertEquals("Clean Code", found.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteBookById_whenBookNotFound_throwsException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(2L));
        verify(bookRepository, times(1)).findById(2L);
        verify(bookRepository, times(0)).deleteById(2L);
    }

    @Test
    public void addBookDTO_whenAdded_returnsBook() {
        final var mockBookDTO = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        final var mockBook = new Book();
        mockBook.setTitle(mockBookDTO.title());
        mockBook.setAuthor(mockBookDTO.author());
        mockBook.setPrice(mockBookDTO.price());
        final var mockBookWithId = new Book();
        mockBookWithId.setId(1L);
        mockBookWithId.setTitle(mockBook.getTitle());
        mockBookWithId.setAuthor(mockBook.getAuthor());
        mockBookWithId.setPrice(mockBook.getPrice());
        when(bookRepository.save(mockBook)).thenReturn(mockBookWithId);
        final var found = bookService.addBook(mockBookDTO);
        assertEquals(1L, found.getId());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    public void addBookDTO_whenNull_returnsNull() {
        final var found = bookService.addBook(null);
        assertNull(found);
    }

    @Test
    public void addListOfBooksDTO_whenAdded_returnsBooks() {
        final var mockBookDTO1 = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        final var mockBook1 = new Book();
        mockBook1.setTitle(mockBookDTO1.title());
        mockBook1.setAuthor(mockBookDTO1.author());
        mockBook1.setPrice(mockBookDTO1.price());
        final var mockBook1WithId = new Book();
        mockBook1WithId.setId(1L);
        mockBook1WithId.setTitle(mockBook1.getTitle());
        mockBook1WithId.setAuthor(mockBook1.getAuthor());
        mockBook1WithId.setPrice(mockBook1.getPrice());
        final var mockBookDTO2 = new BookDTO("Harry Potter", "J.K. Rowling", new BigDecimal("499.99"));
        final var mockBook2 = new Book();
        mockBook2.setTitle(mockBookDTO2.title());
        mockBook2.setAuthor(mockBookDTO2.author());
        mockBook2.setPrice(mockBookDTO2.price());
        final var mockBook2WithId = new Book();
        mockBook2WithId.setId(2L);
        mockBook2WithId.setTitle(mockBook2.getTitle());
        mockBook2WithId.setAuthor(mockBook2.getAuthor());
        mockBook2WithId.setPrice(mockBook2.getPrice());
        final var listOfBookDTOs = List.of(mockBookDTO1, mockBookDTO2);
        final var listOfBooks = List.of(mockBook1, mockBook2);
        final var listOfBooksWithId = List.of(mockBook1WithId, mockBook2WithId);
        when(bookRepository.saveAll(listOfBooks)).thenReturn(listOfBooksWithId);
        final var found = bookService.addBooks(listOfBookDTOs);
        assertEquals(1L, found.getFirst().getId());
        assertEquals(2L, found.get(1).getId());
        verify(bookRepository, times(1)).saveAll(listOfBooks);
    }

    @Test
    public void addListOfBooksDTO_whenNull_returnsNull() {
        final var found = bookService.addBooks(null);
        assertNull(found);
    }

    @Test
    public void updateBookDTO_whenNull_returnsNull() {
        final var found = bookService.updateBook(2L, null);
        assertNull(found);
    }

    @Test
    public void updateBookDTO_whenBookExists_returnUpdatedBook() {
        final var mockBookDTO = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        final var storedBook = new Book();
        storedBook.setId(1L);
        storedBook.setTitle("Harry Potter");
        storedBook.setAuthor("J.K. Rowling");
        storedBook.setPrice(new BigDecimal("499.99"));
        final var updatedStoredBook = new Book();
        updatedStoredBook.setId(1L);
        updatedStoredBook.setTitle(mockBookDTO.title());
        updatedStoredBook.setAuthor(mockBookDTO.author());
        updatedStoredBook.setPrice(mockBookDTO.price());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(storedBook));
        when(bookRepository.save(updatedStoredBook)).thenReturn(updatedStoredBook);
        final var found = bookService.updateBook(1L, mockBookDTO);
        assertEquals("Clean Code", found.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(updatedStoredBook);
    }

    @Test
    public void updateBookDTO_whenBookNotFound_throwsException() {
        final var mockBookDTO = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(2L, mockBookDTO));
        verify(bookRepository, times(1)).findById(2L);
    }
}
