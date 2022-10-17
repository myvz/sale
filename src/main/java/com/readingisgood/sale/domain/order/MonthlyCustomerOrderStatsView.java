package com.readingisgood.sale.domain.order;

import java.math.BigDecimal;

public interface MonthlyCustomerOrderStatsDto {
    int getMonth();
    int getTotalOrderCount();
    int getTotalBookCount();
    BigDecimal getTotalPurchasedAmount();
}
