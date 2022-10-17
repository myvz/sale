package com.readingisgood.sale.domain.customer;

import com.readingisgood.sale.domain.order.MonthlyCustomerOrderStatsView;
import com.readingisgood.sale.domain.order.Order;
import com.readingisgood.sale.domain.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {

    private final OrderRepository orderRepository;

    public CustomerOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<MonthlyCustomerOrderStatsView> getMonthlyCustomerOrderStatsByCustomerId(Long customerId) {
        return orderRepository.findMonthlyCustomerOrderStatsByCustomerId(customerId);
    }

    public Page<Order> getCustomerOrders(Long customerId, int page, int size) {
        Page<Order> orders = orderRepository.findByCustomer_Id(customerId, PageRequest.of(page, size));
        List<Order> ordersFetchedOrderLines = orderRepository.findByIdIn(orders.getContent()
                .stream().map(Order::getId).collect(Collectors.toList()));
        return new PageImpl<>(ordersFetchedOrderLines, Pageable.ofSize(size), orders.getTotalElements());
    }
}
