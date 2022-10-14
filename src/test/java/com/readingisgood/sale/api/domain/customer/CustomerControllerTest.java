package com.readingisgood.sale.api.domain.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.sale.api.security.JJWTAuthenticationFilterConfiguration;
import com.readingisgood.sale.api.security.JwtConfiguration;
import com.readingisgood.sale.domain.customer.Customer;
import com.readingisgood.sale.domain.customer.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@Import(value = {JJWTAuthenticationFilterConfiguration.class, JwtConfiguration.class})
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    CustomerRequestMapper customerRequestMapper;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void it_should_createCustomer() throws Exception {
        CustomerRegisterRequest request = CustomerRegisterRequest.builder()
                .email("test@readingisgood.com")
                .name("john")
                .lastName("doe")
                .build();

        Customer createdCustomer = Customer.builder()
                .name("John")
                .lastName("Doe")
                .email("test@readingisgood.com").build();
        when(customerService.createCustomer(customerRequestMapper.mapToCustomer(request))).thenReturn(createdCustomer);
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").value("SUCCESS"));
    }

    @Test
    void it_should_validate_createCustomerRequest() throws Exception {
        CustomerRegisterRequest request = CustomerRegisterRequest.builder()
                .build();
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("FAILED"));
    }
}