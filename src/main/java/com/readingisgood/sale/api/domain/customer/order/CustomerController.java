package com.readingisgood.sale.api.domain.customer;

import com.readingisgood.sale.domain.customer.Customer;
import com.readingisgood.sale.domain.customer.CustomerOrderService;
import com.readingisgood.sale.domain.customer.CustomerService;
import com.readingisgood.sale.domain.order.Order;
import com.readingisgood.sale.domain.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerOrderService customerOrderService;
    private final CustomerRequestMapper customerRequestMapper;

    public CustomerController(CustomerService customerService,
                              CustomerOrderService customerOrderService,
                              CustomerRequestMapper customerRequestMapper) {
        this.customerService = customerService;
        this.customerOrderService = customerOrderService;
        this.customerRequestMapper = customerRequestMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerRegisterResponse createCustomer(@Validated @RequestBody CustomerRegisterRequest customerRegisterRequest) {
        Customer customer = customerRequestMapper.mapToCustomer(customerRegisterRequest);
        Customer createdCustomer = customerService.createCustomer(customer);
        return customerRequestMapper.mapToCustomerResponse(createdCustomer);
    }

    @GetMapping("/{customerId}/order")
    public CustomerOrderPageResponse getCustomerOrders(@PathVariable("customerId") Long customerId,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Page<Order> customerOrders = customerOrderService.getCustomerOrders(customerId, page, pageSize);
        return
    }
}
