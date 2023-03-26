package com.quotespilot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) throws Exception {
        CustomException customException=new CustomException(new Date(), ex +" while calling " +request.getDescription(false));
        return new ResponseEntity<Object>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
