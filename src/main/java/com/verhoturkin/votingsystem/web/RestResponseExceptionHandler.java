package com.verhoturkin.votingsystem.web;

import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler({NotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity notFoundError(HttpServletRequest req, Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDenied(HttpServletRequest req, Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
