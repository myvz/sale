package com.readingisgood.sale.api.domain.book;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.book.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookMapper bookMapper;
    private final BookService bookService;

    public BookController(BookMapper bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewBookResponse createNewBook(@Validated @RequestBody NewBookRequest newBookRequest) {
        Book newBook = bookService.createNewBook(bookMapper.mapToBook(newBookRequest));
        return bookMapper.mapToBookResponse(newBook);
    }

    @PatchMapping("/{bookId}")
    public NewBookResponse updateBook(@PathVariable("bookId") Long bookId, @Validated @RequestBody BookUpdateRequest bookUpdateRequest) {
        Book book = bookService.updateBook(bookId, bookMapper.mapToBookUpdateDto(bookUpdateRequest));
        return bookMapper.mapToBookResponse(book);
    }
}
