package com.readingisgood.sale.api.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
@Value
@Builder
public class OrderDetailResponse {
    OrderResponse order;
    List<OrderLineResponse> orderLines;

    @Value
    @Builder
    static class OrderResponse {
        Long orderId;
        Long customerId;
    }

    @Value
    @Builder
    static class OrderLineResponse {
        Long orderLineId;
        Long bookId;
    }
}
