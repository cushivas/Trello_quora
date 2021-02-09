package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


// This annotation allow to write a code which will be made available to all controller class
@ControllerAdvice

public class RestExceptionHandler {

    // The @ExceptionHandler allow you to define a method to handle specific exception
    @ExceptionHandler(SignUpRestrictedException.class)

    //The signupException method take two arguments the exception object and the web object it decide the action to be taken when the exception occur
    public ResponseEntity<ErrorResponse> signupException(SignUpRestrictedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.CONFLICT
        );
    }
}
