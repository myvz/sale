package com.readingisgood.sale.api.domain.customer;

import com.readingisgood.sale.domain.customer.Customer;
import com.readingisgood.sale.domain.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRequestMapper customerRequestMapper;

    public CustomerController(CustomerService customerService,
                              CustomerRequestMapper customerRequestMapper) {
        this.customerService = customerService;
        this.customerRequestMapper = customerRequestMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerRegisterResponse createCustomer(@Validated @RequestBody CustomerRegisterRequest customerRegisterRequest) {
        Customer customer = customerRequestMapper.mapToCustomer(customerRegisterRequest);
        Customer createdCustomer = customerService.createCustomer(customer);
        return customerRequestMapper.mapToCustomerResponse(createdCustomer);
    }
}
