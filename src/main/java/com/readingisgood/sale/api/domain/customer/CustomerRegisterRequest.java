package com.readingisgood.sale.api.domain.customer;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerRegisterRequest {
    @Email
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastName;
}
