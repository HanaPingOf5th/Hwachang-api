package com.hwachang.hwachangapi.utils.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

@Getter
public class CustomExceptionData {
    private final HttpStatus httpStatus;
    private final String exceptionName;
    private final LocalDateTime timestamp;

    private CustomExceptionData(HttpStatus httpStatus, String exceptionName) {
        this.httpStatus = httpStatus;
        this.exceptionName = exceptionName;
        this.timestamp = LocalDateTime.now();
    }

    public static CustomExceptionData create(HttpStatus httpStatus, String exceptionMessage){
        return new CustomExceptionData(httpStatus, exceptionMessage);
    }
}
