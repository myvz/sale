package com.readingisgood.sale.domain.order;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.customer.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderCreateMapper {

    public Order mapToOrder(CreteOrder creteOrder) {
        return Order.builder().customer
                (Customer.builder()
                        .id(creteOrder.getCustomerId())
                        .build()
                ).build();
    }

    public List<OrderLine> mapToOrderLines(CreteOrder creteOrder, Order order) {
        return creteOrder.getOrderLines().stream()
                .map(orderLine -> OrderLine.builder()
                        .book(Book.builder().id(orderLine.getBookId()).build())
                        .quantity(orderLine.getQuantity())
                        .order(order)
                        .build())
                .collect(Collectors.toList());
    }
}
