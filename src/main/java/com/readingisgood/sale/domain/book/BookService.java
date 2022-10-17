package com.readingisgood.sale.domain.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book createNewBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book updateBook(long bookId, BookUpdate bookUpdate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found"));
        var newStockCount = bookUpdate.getStockCount() != null ? bookUpdate.getStockCount() : book.getStockCount();
        var newPrice = bookUpdate.getPrice() != null ? bookUpdate.getPrice() : book.getPrice();
        book.setStockCount(newStockCount);
        book.setPrice(newPrice);
        return book;
    }
}
