package com.readingisgood.sale.domain.order;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CreteOrder {

    Long customerId;
    @Singular
    List<OrderLine> orderLines;

    @Value
    @Builder
    public static class OrderLine {
        Long bookId;
        Integer quantity;
    }

}
