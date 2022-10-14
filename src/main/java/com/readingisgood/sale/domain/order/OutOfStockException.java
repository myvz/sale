package com.readingisgood.sale.domain.order;

import com.readingisgood.sale.domain.exception.BusinessException;

public class OutOfStockException extends BusinessException {
    public OutOfStockException() {
        super("out-of-stock", "Stocks are unavailable");
    }
}
