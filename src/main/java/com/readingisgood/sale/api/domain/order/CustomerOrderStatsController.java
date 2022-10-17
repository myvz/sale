package com.readingisgood.sale.api.domain.order;

import com.readingisgood.sale.domain.customer.CustomerOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/customer/{customerId}/order-stats")
public class CustomerOrderStatsController {


    private final CustomerOrderService customerOrderService;
    private final CustomerOrderStatsMapper customerOrderStatsMapper;

    public CustomerOrderStatsController(CustomerOrderService customerOrderService, CustomerOrderStatsMapper customerOrderStatsMapper) {
        this.customerOrderService = customerOrderService;
        this.customerOrderStatsMapper = customerOrderStatsMapper;
    }

    @GetMapping
    public List<CustomerOrderStatsResponse> getMonthlyCustomerOrderStats(@PathVariable("customerId") Long customerId) {
        var stats = customerOrderService.getMonthlyCustomerOrderStatsByCustomerId(customerId);
        return customerOrderStatsMapper.mapToCustomerOrderStatsResponse(stats);
    }
}
