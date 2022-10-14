package com.readingisgood.sale.domain.exception;

import lombok.*;

@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    @Builder
    public BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
