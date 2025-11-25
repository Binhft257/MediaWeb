package com.javaweb.exceptions;

import com.javaweb.model.response.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return new ApiResponse<>(false, message, null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }
}
