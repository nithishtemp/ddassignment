package com.dd.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javassist.NotFoundException;

@ControllerAdvice
public class CommonExceptionHandler{
	
    @ResponseStatus(code=HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException ex) {
    	ErrorMessage errorMessage = new ErrorMessage(12, ex.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
    }
	
    @ResponseStatus(code=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
    	ErrorMessage errorMessage = new ErrorMessage(10, ex.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    
    @ResponseStatus(code=HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ErrorMessage> handleIOException(IOException ex) {
    	ErrorMessage errorMessage = new ErrorMessage(11, ex.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
    } 
    
}
