package com.readingisgood.sale.api.domain.customer.order;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderDto {

    Long orderId;

    @Singular
    List<OrderLineDto> orderLines;

    LocalDateTime orderTime;
}
