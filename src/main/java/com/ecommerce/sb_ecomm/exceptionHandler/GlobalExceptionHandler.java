package com.ecommerce.sb_ecomm.exceptionHandler;


import com.ecommerce.sb_ecomm.exception.APIException;
import com.ecommerce.sb_ecomm.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
//rest controller advice making it global
public class GlobalExceptionHandler {

    //handle this exception using this method and mapping it using the ExceptionHandler with Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            //this will give me the field name which is coming empty
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            response.put(fieldName, message);
        });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        ApiResponse  response = new ApiResponse(message, false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponse> myApiException(APIException e) {
        String message = e.getMessage();
        ApiResponse  response = new ApiResponse(message, false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
