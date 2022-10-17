package com.readingisgood.sale.domain.order;

import java.math.BigDecimal;

public interface MonthlyCustomerOrderStatsView {
    int getMonth();
    int getTotalOrderCount();
    int getTotalBookCount();
    BigDecimal getTotalPurchasedAmount();
}
