package com.amunoz.springboot.webflux.ticcajica.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CourseNotExistException extends RuntimeException{

    public CourseNotExistException(String message) {
        super(message);
    }
}

