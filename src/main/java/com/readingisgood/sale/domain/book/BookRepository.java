package com.readingisgood.sale.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("update Book set stockCount=stockCount - :soldItem where stockCount - :soldItem >=0 and id = :bookId")
    int decreaseStock(@Param("bookId") long bookId, @Param("soldItem") long soldItem);
}
