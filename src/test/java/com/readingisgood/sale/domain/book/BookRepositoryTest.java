package com.readingisgood.sale.domain.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    void decreaseStock() {
        Book book = Book.builder().ISBN("978-1-56619-909-4")
                .author("9780132350884")
                .name("Robert Martin")
                .genre("Software Design")
                .stockCount(1000L)
                .price(BigDecimal.valueOf(136))
                .build();
        testEntityManager.persistAndFlush(book);
        testEntityManager.clear();

        //when
        int updatedRowCount = bookRepository.decreaseStock(book.getId(), 3);


        //then
        assertThat(updatedRowCount).isEqualTo(1);
        Optional<Book> updated = bookRepository.findById(book.getId());
        assertThat(updated).hasValueSatisfying(book1 ->
                assertThat(book1.getStockCount()).isEqualTo(997));
    }
}