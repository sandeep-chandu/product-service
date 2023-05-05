package com.cakestore.productservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.cakestore.productservice.dto.ApiError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler{
	
	
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex) {
    	 List<String> details = new ArrayList<String>();
         details.add(ex.getMessage());
         
         ApiError err = new ApiError(
             LocalDateTime.now(),
             HttpStatus.NOT_FOUND, 
             "Resource Not Found" ,
             details);
        
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    	List<String> details = new ArrayList<String>();
        details = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField()+ " : " +error.getDefaultMessage())
                    .collect(Collectors.toList());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Validation Errors" ,
            details);
        
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
      
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Type Mismatch" ,
            details);
        
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex) {
        
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Constraint Violations" ,
            details);
        
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(Exception ex) {
        
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            "Invalid content type" ,
            details);
        
        return new ResponseEntity<>(err, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handle403Errors(Exception ex) {
        ex.printStackTrace();
        List<String> details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN, 
            "Insufficient access" ,
            details);
        
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAllErrors(Exception ex) {
        ex.printStackTrace();
        List<String> details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Error occurred" ,
            details);
        
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
