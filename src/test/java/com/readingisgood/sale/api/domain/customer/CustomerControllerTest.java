package com.readingisgood.sale.api.domain.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.sale.api.domain.customer.order.CustomerController;
import com.readingisgood.sale.api.domain.customer.order.CustomerOrderMapper;
import com.readingisgood.sale.api.security.JJWTAuthenticationFilterConfiguration;
import com.readingisgood.sale.api.security.JwtConfiguration;
import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.customer.Customer;
import com.readingisgood.sale.domain.customer.CustomerOrderService;
import com.readingisgood.sale.domain.customer.CustomerService;
import com.readingisgood.sale.domain.order.Order;
import com.readingisgood.sale.domain.order.OrderLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @SpyBean
    CustomerOrderMapper customerOrderMapper;

    @MockBean
    CustomerService customerService;

    @MockBean
    CustomerOrderService customerOrderService;

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

    @Test
    void it_should_get_customer_orders() throws Exception {
        long customerId = 12;
        LocalDateTime createdDate = LocalDateTime.now();

        Book book = Book.builder().ISBN("978-1-56619-909-4")
                .author("Robert Martin")
                .name("Clean Code")
                .genre("Software Design")
                .stockCount(1000L)
                .build();
        Order order = Order.builder()
                .orderLines(List.of(
                        OrderLine.builder().quantity(2).book(book).build())
                ).build();
        order.setCreatedDate(createdDate);

        when(customerOrderService.getCustomerOrders(customerId, 0, 10))
                .thenReturn(new PageImpl<>(List.of(order)));

        mockMvc.perform(get("/customer/{customerId}/order", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.totalPage").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].quantity").value(2))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].book.name").value(book.getName()))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].book.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].book.genre").value(book.getGenre()))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].book.stockCount").value(book.getStockCount()))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].book.price").value(book.getPrice()))
                .andExpect(jsonPath("$.data.orders.[0].orderLines.[0].book.isbn").value(book.getISBN()));
    }
}