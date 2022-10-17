package com.readingisgood.sale.domain.order;

import com.readingisgood.sale.domain.book.BookRepository;
import com.readingisgood.sale.domain.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final CustomerRepository customerRepository;

    public OrderService(BookRepository bookRepository,
                        OrderRepository orderRepository,
                        OrderLineRepository orderLineRepository,
                        CustomerRepository customerRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.customerRepository = customerRepository;
    }


    @Transactional
    public Order order(CreteOrder createOrder) {
        if (isOutOffStockAnyItem(createOrder)) {
            throw new OutOfStockException();
        }
        Order order = createOrder(createOrder);
        List<OrderLine> orderLines = createOrderLines(createOrder, order);
        order.setOrderLines(orderLines);
        return order;
    }

    private boolean isOutOffStockAnyItem(CreteOrder createOrder) {
        return createOrder.getOrderLines().stream()
                .map(ol -> bookRepository.decreaseStock(ol.getBookId(), ol.getQuantity()))
                .anyMatch(updatedStockCount -> updatedStockCount == 0);
    }

    private Order createOrder(CreteOrder createOrder) {
        return orderRepository.save(mapToOrder(createOrder));
    }

    private List<OrderLine> createOrderLines(CreteOrder createOrder, Order order) {
        return orderLineRepository.saveAll(mapToOrderLines(createOrder, order));
    }

    private Order mapToOrder(CreteOrder creteOrder) {
        return Order.builder().customer(customerRepository.getReferenceById(creteOrder.getCustomerId())).build();
    }

    private List<OrderLine> mapToOrderLines(CreteOrder creteOrder, Order order) {
        return creteOrder.getOrderLines().stream()
                .map(orderLine -> OrderLine.builder()
                        .book(bookRepository.getReferenceById(orderLine.getBookId()))
                        .quantity(orderLine.getQuantity())
                        .order(order)
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrder(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedDateBetween(startDate, endDate);
    }
}
