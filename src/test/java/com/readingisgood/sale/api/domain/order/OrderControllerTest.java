package com.readingisgood.sale.api.domain.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.sale.api.domain.book.BookMapper;
import com.readingisgood.sale.api.domain.customer.CustomerController;
import com.readingisgood.sale.api.security.JJWTAuthenticationFilterConfiguration;
import com.readingisgood.sale.api.security.JwtConfiguration;
import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.book.BookService;
import com.readingisgood.sale.domain.customer.Customer;
import com.readingisgood.sale.domain.order.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@Import(value = {JJWTAuthenticationFilterConfiguration.class, JwtConfiguration.class})
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    OrderMapper orderMapper;

    @SpyBean
    OrderCreateMapper orderCreateMapper;

    @MockBean
    OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createOrder() throws Exception {
        Long customerId = 123L;
        OrderRequest orderRequest = OrderRequest.builder()
                .customerId(customerId)
                .orderLineRequest(OrderRequest.OrderLineRequest.builder()
                        .bookId(23L)
                        .quantity(3)
                        .build())
                .orderLineRequest(OrderRequest.OrderLineRequest.builder()
                        .bookId(24L)
                        .quantity(1)
                        .build())
                .build();

        Order order = Order.builder()
                .id(989L)
                .customer(Customer.builder().id(customerId).build())
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .orderLine(OrderLine.builder()
                        .id(11L)
                        .order(order)
                        .book(Book.builder().id(23L).build())
                        .build())
                .orderLine(OrderLine.builder()
                        .id(12L)
                        .order(order)
                        .book(Book.builder().id(24L).build())
                        .build())
                .build();


        when(orderService.order(orderMapper.mapToCreateOrder(orderRequest))).thenReturn(orderDetail);

        mockMvc.perform(post("/order")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.order.orderId").value(order.getId()))
                .andExpect(jsonPath("$.data.order.customerId").value(customerId))
                .andExpect(jsonPath("$.data.orderLines.[?(@.orderLineId==11)].bookId").value(23))
                .andExpect(jsonPath("$.data.orderLines.[?(@.orderLineId==12)].bookId").value(24));
    }

    @Test
    void it_should_return_bad_request_when_stocks_are_unavailable_createOrder() throws Exception {
        Long customerId = 123L;
        OrderRequest orderRequest = OrderRequest.builder()
                .customerId(customerId)
                .orderLineRequest(OrderRequest.OrderLineRequest.builder()
                        .bookId(23L)
                        .quantity(3)
                        .build())
                .orderLineRequest(OrderRequest.OrderLineRequest.builder()
                        .bookId(24L)
                        .quantity(1)
                        .build())
                .build();

        when(orderService.order(orderMapper.mapToCreateOrder(orderRequest))).thenThrow(new OutOfStockException());

        mockMvc.perform(post("/order")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("FAILED"))
                .andExpect(jsonPath("$.errors.[0].errorCode").value("out-of-stock"));
    }

    @Test
    void it_should_get_order_detail() throws Exception {
        var orderId = 13L;
        var customerId = 123L;
        Order order = Order.builder()
                .id(989L)
                .customer(Customer.builder().id(customerId).build())
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .orderLine(OrderLine.builder()
                        .id(11L)
                        .order(order)
                        .book(Book.builder().id(23L).build())
                        .build())
                .orderLine(OrderLine.builder()
                        .id(12L)
                        .order(order)
                        .book(Book.builder().id(24L).build())
                        .build())
                .build();

        when(orderService.getOrder(orderId)).thenReturn(Optional.of(orderDetail));

        mockMvc.perform(get("/order/{orderId}", orderId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.order.orderId").value(order.getId()))
                .andExpect(jsonPath("$.data.order.customerId").value(customerId))
                .andExpect(jsonPath("$.data.orderLines.[?(@.orderLineId==11)].bookId").value(23))
                .andExpect(jsonPath("$.data.orderLines.[?(@.orderLineId==12)].bookId").value(24));

    }

    @Test
    void it_should_return_404_when_order_not_found() throws Exception {

        when(orderService.getOrder(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/order/{orderId}", anyLong())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value("SUCCESS"));
    }


    @Test
    void it_should_get_order_detail_by_created_date() throws Exception {
        var orderId = 13L;
        var customerId = 123L;
        Order order = Order.builder()
                .id(989L)
                .customer(Customer.builder().id(customerId).build())
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .orderLine(OrderLine.builder()
                        .id(11L)
                        .order(order)
                        .book(Book.builder().id(23L).build())
                        .build())
                .orderLine(OrderLine.builder()
                        .id(12L)
                        .order(order)
                        .book(Book.builder().id(24L).build())
                        .build())
                .build();

        LocalDateTime startDate = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);;

        when(orderService.getOrder(startDate,endDate)).thenReturn(List.of(orderDetail));

        mockMvc.perform(get("/order", orderId)
                        .param("startDate",String.valueOf(startDate.atZone(ZoneId.systemDefault()).toEpochSecond()))
                        .param("endDate",String.valueOf(endDate.atZone(ZoneId.systemDefault()).toEpochSecond()))
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.[0].order.orderId").value(order.getId()))
                .andExpect(jsonPath("$.data.[0].order.customerId").value(customerId))
                .andExpect(jsonPath("$.data.[0].orderLines.[?(@.orderLineId==11)].bookId").value(23))
                .andExpect(jsonPath("$.data.[0].orderLines.[?(@.orderLineId==12)].bookId").value(24));
    }
}