package com.readingisgood.sale.api.domain.book;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.book.BookUpdate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<BookResponse> mapToBookResponse(List<Book> books) {
        return books.stream()
                .map(this::mapToBookResponse)
                .collect(Collectors.toList());
    }

    public BookResponse mapToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
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
