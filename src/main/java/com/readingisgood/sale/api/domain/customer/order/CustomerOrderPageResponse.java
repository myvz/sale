package com.readingisgood.sale.api.domain.customer.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Builder
@Getter
@Setter
@Value
public class CustomerOrderPageResponse {
    int totalPage;
    long totalElements;
    List<OrderDto> orders;
}
