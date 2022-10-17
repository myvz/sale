package com.readingisgood.sale.api.domain.order;

import com.readingisgood.sale.domain.order.CreteOrder;
import com.readingisgood.sale.domain.order.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.readingisgood.sale.api.domain.order.OrderDetailResponse.OrderLineResponse;
import static com.readingisgood.sale.api.domain.order.OrderDetailResponse.OrderResponse;

@Component
public class OrderMapper {

    public CreteOrder mapToCreateOrder(OrderRequest orderRequest) {
        return CreteOrder.builder()
                .customerId(orderRequest.getCustomerId())
                .orderLines(orderRequest.getOrderLineRequests().stream()
                        .map(ol -> CreteOrder.OrderLine.builder()
                                .quantity(ol.getQuantity())
                                .bookId(ol.getBookId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderDetailResponse mapToOrderResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .order(OrderResponse.builder().orderId(orderDetail.getOrder().getId())
                        .customerId(orderDetail.getOrder().getCustomer().getId())
                        .build())
                .orderLines(orderDetail.getOrderLines()
                        .stream().map(ol -> OrderLineResponse.builder()
                                .orderLineId(ol.getId())
                                .bookId(ol.getBook().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
