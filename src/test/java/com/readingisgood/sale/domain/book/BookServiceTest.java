package com.readingisgood.sale.domain.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    @Test
    void it_should_createNewBook() {
        var book = Book.builder().ISBN("978-1-56619-909-4")
                .author("9780132350884")
                .name("Robert Martin")
                .genre("Software Design")
                .stockCount(1000L)
                .build();
        bookService.createNewBook(book);

        verify(bookRepository).save(book);
    }

    @Test
    void it_should_updateBook() {
        //given
        var id = 100L;
        var book = Book.builder().ISBN("978-1-56619-909-4")
                .author("9780132350884")
                .name("Robert Martin")
                .genre("Software Design")
                .stockCount(1000L)
                .id(id)
                .build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        long newStockCount = 259L;

        //when
        Book updatedBook = bookService.updateBook(id, BookUpdate.builder().stockCount(newStockCount).build());

        //then
        assertThat(updatedBook.getStockCount()).isEqualTo(newStockCount);
    }

    @Test
    void it_should_do_Nothing_when_fields_are_Null() {
        //given
        var id = 100L;
        long stockCount = 1000L;
        var book = Book.builder().ISBN("978-1-56619-909-4")
                .author("9780132350884")
                .name("Robert Martin")
                .genre("Software Design")
                .stockCount(stockCount)
                .id(id)
                .build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        //when
        Book updatedBook = bookService.updateBook(id, BookUpdate.builder().build());

        //then
        assertThat(updatedBook.getStockCount()).isEqualTo(stockCount);
    }
}