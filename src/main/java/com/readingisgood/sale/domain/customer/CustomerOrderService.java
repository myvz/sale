package com.readingisgood.sale.domain.customer;

import com.readingisgood.sale.domain.order.MonthlyCustomerOrderStatsView;
import com.readingisgood.sale.domain.order.Order;
import com.readingisgood.sale.domain.order.OrderLineRepository;
import com.readingisgood.sale.domain.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    public CustomerOrderService(OrderRepository orderRepository,
                                OrderLineRepository orderLineRepository) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
    }

    public List<MonthlyCustomerOrderStatsView> getMonthlyCustomerOrderStatsByCustomerId(Long customerId) {
        return orderLineRepository.findMonthlyCustomerOrderStatsByCustomerId(customerId);
    }

    public Page<Order> getCustomerOrders(Long customerId, int page, int size) {
        return orderRepository.findByCustomer_Id(customerId, PageRequest.of(page, size));
    }
}
