package com.readingisgood.sale.api.domain.book;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;


@Value
@Builder
public class BookResponse {
    Long id;
    String name;
    String author;
    String genre;
    String ISBN;
    Long stockCount;
    BigDecimal price;
}
