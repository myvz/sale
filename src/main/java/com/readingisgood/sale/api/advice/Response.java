package com.readingisgood.sale.api.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Result result;
    private Object data;
    @Singular
    private List<Error> errors;

    public enum Result {SUCCESS, FAILED}

    @Builder
    @Getter
    @Setter
    public static class Error {
        String errorCode;
        String errorMessage;
    }
}
