package com.readingisgood.sale.api.domain.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.sale.api.security.JJWTAuthenticationFilterConfiguration;
import com.readingisgood.sale.api.security.JwtConfiguration;
import com.readingisgood.sale.domain.customer.CustomerOrderService;
import com.readingisgood.sale.domain.order.MonthlyCustomerOrderStatsView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CustomerOrderStatsController.class)
@Import(value = {JJWTAuthenticationFilterConfiguration.class, JwtConfiguration.class})
class CustomerOrderStatsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    CustomerOrderStatsMapper customerOrderStatsMapper;

    @MockBean
    CustomerOrderService customerOrderService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void it_should_getMonthlyCustomerOrderStats() throws Exception {
        long customerId = 3L;
        MonthlyCustomerOrderStatsView mock = Mockito.mock(MonthlyCustomerOrderStatsView.class);
        when(mock.getMonth()).thenReturn(4);
        when(mock.getTotalOrderCount()).thenReturn(2);
        when(mock.getTotalBookCount()).thenReturn(5);
        when(mock.getTotalPurchasedAmount()).thenReturn(BigDecimal.valueOf(124.35));
        when(customerOrderService.getMonthlyCustomerOrderStatsByCustomerId(customerId)).thenReturn(List.of(mock));

        mockMvc.perform(get("/customer/{customerId}/order-stats", customerId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.[0].month").value(4))
                .andExpect(jsonPath("$.data.[0].totalOrderCount").value(2))
                .andExpect(jsonPath("$.data.[0].totalBookCount").value(5))
                .andExpect(jsonPath("$.data.[0].totalPurchasedAmount").value(124.35));
    }
}