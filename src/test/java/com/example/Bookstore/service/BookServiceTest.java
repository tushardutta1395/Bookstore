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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        final var mockBook1 = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .price(new BigDecimal("40.0"))
                .build();
        final var mockBook2 = Book.builder()
                .id(2L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(new BigDecimal("599.99"))
                .build();
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
        final var mockBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .price(new BigDecimal("40.0"))
                .build();
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
        final var mockBook1 = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .price(new BigDecimal("40.0"))
                .build();
        final var mockBook2 = Book.builder()
                .id(2L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(new BigDecimal("599.99"))
                .build();
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
        final var mockBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .price(new BigDecimal("40.0"))
                .build();

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
        final var mockBook = Book.builder()
                .title(mockBookDTO.title())
                .author(mockBookDTO.author())
                .price(mockBookDTO.price())
                .build();
        final var mockBookWithId = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .price(new BigDecimal("40.0"))
                .build();
        when(bookRepository.save(mockBook)).thenReturn(mockBookWithId);
        final var found = bookService.addBook(mockBookDTO);
        assertEquals(1L, found.getId());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    public void addListOfBooksDTO_whenAdded_returnsBooks() {
        final var mockBookDTO1 = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        final var mockBook1 = Book.builder()
                .title(mockBookDTO1.title())
                .author(mockBookDTO1.author())
                .price(mockBookDTO1.price())
                .build();
        final var mockBook1WithId = Book.builder()
                .id(1L)
                .title(mockBook1.getTitle())
                .author(mockBook1.getAuthor())
                .price(mockBook1.getPrice())
                .build();
        final var mockBookDTO2 = new BookDTO("Harry Potter", "J.K. Rowling", new BigDecimal("499.99"));
        final var mockBook2 = Book.builder()
                .title(mockBookDTO2.title())
                .author(mockBookDTO2.author())
                .price(mockBookDTO2.price())
                .build();
        final var mockBook2WithId = Book.builder()
                .id(2L)
                .title(mockBook2.getTitle())
                .author(mockBook2.getAuthor())
                .price(mockBook2.getPrice())
                .build();
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
        final var found = bookService.addBooks((List<BookDTO>) null);
        assertNull(found);
        verify(bookRepository, times(0)).saveAll(null);
    }

    @Test
    public void addVarArgsBooksDTO_whenAdded_returnsBooks() {
        final var mockBookDTO1 = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        final var mockBook1 = Book.builder()
                .title(mockBookDTO1.title())
                .author(mockBookDTO1.author())
                .price(mockBookDTO1.price())
                .build();
        final var mockBook1WithId = Book.builder()
                .id(1L)
                .title(mockBook1.getTitle())
                .author(mockBook1.getAuthor())
                .price(mockBook1.getPrice())
                .build();
        final var mockBookDTO2 = new BookDTO("Harry Potter", "J.K. Rowling", new BigDecimal("499.99"));
        final var mockBook2 = Book.builder()
                .title(mockBookDTO2.title())
                .author(mockBookDTO2.author())
                .price(mockBookDTO2.price())
                .build();
        final var mockBook2WithId = Book.builder()
                .id(2L)
                .title(mockBook2.getTitle())
                .author(mockBook2.getAuthor())
                .price(mockBook2.getPrice())
                .build();
        final var listOfBooks = List.of(mockBook1, mockBook2);
        final var listOfBooksWithId = List.of(mockBook1WithId, mockBook2WithId);
        when(bookRepository.saveAll(listOfBooks)).thenReturn(listOfBooksWithId);
        final var found = bookService.addBooks(mockBookDTO1, mockBookDTO2);
        assertEquals(1L, found.getFirst().getId());
        assertEquals(2L, found.get(1).getId());
        verify(bookRepository, times(1)).saveAll(listOfBooks);
    }

    @Test
    public void addVarArgsBooksDTO_whenNull_returnsNull() {
        final var found = bookService.addBooks(null, null);
        assertEquals(List.of(), found);
        verify(bookRepository, times(0)).saveAll(null);
    }

    @Test
    public void updateBookDTO_whenNull_returnsNull() {
        final var found = bookService.updateBook(2L, null);
        assertNull(found);
        verify(bookRepository, times(0)).save(null);
    }

    @Test
    public void updateBookDTO_whenBookExists_returnUpdatedBook() {
        final var mockBookDTO = new BookDTO("Clean Code", "Robert C. Martin", new BigDecimal("40.0"));
        final var storedBook = Book.builder()
                .id(1L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(new BigDecimal("499.99"))
                .build();
        final var updatedStoredBook = Book.builder()
                .id(1L)
                .title(mockBookDTO.title())
                .author(mockBookDTO.author())
                .price(mockBookDTO.price())
                .build();
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
