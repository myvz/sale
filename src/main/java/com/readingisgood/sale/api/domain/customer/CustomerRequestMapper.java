package com.readingisgood.sale.api.domain.customer;

import com.readingisgood.sale.domain.customer.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerRequestMapper {

    public Customer mapToCustomer(CustomerRegisterRequest customerRegisterRequest) {
        return Customer.builder()
                .name(customerRegisterRequest.getName())
                .lastName(customerRegisterRequest.getLastName())
                .email(customerRegisterRequest.getEmail())
                .build();
    }

    public CustomerRegisterResponse mapToCustomerResponse(Customer customer) {
        return CustomerRegisterResponse.builder()
                .name(customer.getName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .build();
    }
}
