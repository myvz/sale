package com.readingisgood.sale.api.domain.customer.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BookDto {
    private String name;
    private String author;
    private String genre;
    private String ISBN;
    private Long stockCount;
    private BigDecimal price;
}
