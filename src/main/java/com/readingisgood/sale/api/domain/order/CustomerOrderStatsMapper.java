package com.readingisgood.sale.api.domain.order;

import com.readingisgood.sale.domain.order.MonthlyCustomerOrderStatsView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerOrderStatsMapper {

    public CustomerOrderStatsResponse mapToCustomerOrderStatsResponse(MonthlyCustomerOrderStatsView monthlyCustomerOrderStatsView) {
        return CustomerOrderStatsResponse.builder()
                .month(monthlyCustomerOrderStatsView.getMonth())
                .totalOrderCount(monthlyCustomerOrderStatsView.getTotalOrderCount())
                .totalBookCount(monthlyCustomerOrderStatsView.getTotalBookCount())
                .totalPurchasedAmount(monthlyCustomerOrderStatsView.getTotalPurchasedAmount())
                .build();
    }

    public List<CustomerOrderStatsResponse> mapToCustomerOrderStatsResponse(List<MonthlyCustomerOrderStatsView> monthlyCustomerOrderStatsViews) {
        return monthlyCustomerOrderStatsViews.stream()
                .map(this::mapToCustomerOrderStatsResponse)
                .collect(Collectors.toList());
    }


}
