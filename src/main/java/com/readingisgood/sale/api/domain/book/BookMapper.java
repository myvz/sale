package com.readingisgood.sale.api.domain.book;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.book.BookUpdate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookMapper {

    public Book mapToBook(NewBookRequest newBookRequest) {
        return Book.builder().name(newBookRequest.getName())
                .genre(newBookRequest.getGenre())
                .author(newBookRequest.getAuthor())
                .ISBN(newBookRequest.getISBN())
                .stockCount(newBookRequest.getStockCount())
                .price(newBookRequest.getPrice())
                .build();
    }

    public NewBookResponse mapToBookResponse(Book book) {
        return NewBookResponse.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .ISBN(book.getISBN())
                .stockCount(book.getStockCount())
                .price(book.getPrice())
                .build();
    }

    public BookUpdate mapToBookUpdateDto(BookUpdateRequest bookUpdateRequest) {
        return BookUpdate.builder()
                .stockCount(bookUpdateRequest.getNewStockCount())
                .price(bookUpdateRequest.getPrice())
                .build();
    }
}
