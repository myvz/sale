package com.readingisgood.sale.domain.order;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderDetail {
    Order order;
    @Singular
    List<OrderLine> orderLines;
}
