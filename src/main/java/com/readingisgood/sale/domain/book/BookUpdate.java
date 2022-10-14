package com.readingisgood.sale.domain.book;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class BookUpdate {
    Long stockCount;
    BigDecimal price;
}
