package com.readingisgood.sale.api.domain.customer.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineDto {
    private BookDto book;
    private int quantity;
}
