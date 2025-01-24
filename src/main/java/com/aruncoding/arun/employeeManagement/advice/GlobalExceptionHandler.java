package com.aruncoding.arun.employeeManagement.advice;

import com.aruncoding.arun.employeeManagement.exceptions.ResourceNotFoundException;
import com.aruncoding.arun.employeeManagement.exceptions.RuntimeConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e) {

        ApiError apiError = ApiError
                                .builder()
                                .message(e.getMessage())
                                .status(HttpStatus.NOT_FOUND)
                                .build();
        return helper(apiError);
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiResponse<?>> hanldeRuntimeConflictException(RuntimeConflictException e) {

        ApiError apiError = ApiError
                .builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
        return helper(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputValidationException(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError
                                .builder()
                                .message("Invalid Input Fields")
                                .subErrors(errors)
                                .status(HttpStatus.BAD_REQUEST)
                                .build();

        return helper(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception e){

        ApiError apiError = ApiError
                                .builder()
                                .message(e.getMessage())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build();
        return helper(apiError);
    }


    private ResponseEntity<ApiResponse<?>> helper(ApiError apiError){
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getStatus());
    }

}
