package com.readingisgood.sale.api.advice;

import com.readingisgood.sale.domain.exception.BusinessException;
import com.readingisgood.sale.domain.order.OutOfStockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiResponseExceptionHandler {

    public static final Response.Error UNKNOWN_ERROR = Response.Error.builder()
            .errorCode("unknown-error")
            .errorMessage("Unknown error has occurred.")
            .build();

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Response.Error> errors = ex.getBindingResult().getAllErrors().stream().map((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            return Response.Error.builder().errorCode("invalid-arg").errorMessage(fieldName + " " + errorMessage).build();
        }).collect(Collectors.toList());
        return Response.builder().result(Response.Result.FAILED).errors(errors).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public Response handleException(BusinessException ex) {
        return Response.builder()
                .result(Response.Result.FAILED)
                .error(Response.Error.builder().errorCode(ex.getErrorCode()).errorMessage(ex.getMessage()).build())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception ex) {
        log.error("Unkonw Error", ex);
        return Response.builder()
                .result(Response.Result.FAILED)
                .error(UNKNOWN_ERROR)
                .build();
    }

}
