package com.readingisgood.sale.api.domain.customer.order;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.order.Order;
import com.readingisgood.sale.domain.order.OrderLine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerOrderMapper {

    public List<OrderDto> mapToOrderDto(List<Order> orders) {
        return orders.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto mapToOrderDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .orderTime(order.getCreatedDate())
                .orderLines(order.getOrderLines().stream()
                        .map(this::mapOrderLine)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderLineDto mapOrderLine(OrderLine orderLine) {
        return OrderLineDto.builder()
                .book(this.mapToBookDto(orderLine.getBook()))
                .quantity(orderLine.getQuantity())
                .build();
    }

    private BookDto mapToBookDto(Book book) {
        return BookDto.builder().name(book.getName())
                .genre(book.getGenre())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .stockCount(book.getStockCount())
                .price(book.getPrice())
                .build();
    }
}
