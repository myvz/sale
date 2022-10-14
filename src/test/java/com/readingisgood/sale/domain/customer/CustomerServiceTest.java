package com.readingisgood.sale.domain.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Test
    void createCustomer() {
        //given
        Customer customer = Customer.builder()
                .name("john")
                .lastName("doe")
                .email("johndoe@readingisgood.com")
                .build();

        //when
        customerService.createCustomer(customer);

        //then
        verify(customerRepository).save(customer);
    }
}