package com.readingisgood.sale.api.domain.customer;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerRegisterResponse {
    Long id;
    String email;
    String name;
    String lastName;
}
