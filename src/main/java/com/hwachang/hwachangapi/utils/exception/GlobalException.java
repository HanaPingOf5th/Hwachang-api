package com.hwachang.hwachangapi.utils.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public abstract class GlobalException extends RuntimeException {
    protected CustomExceptionData customExceptionData;
    public GlobalException() {super();}
    public GlobalException(String message, Throwable cause) {
        super(message,cause);
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();
    public abstract LocalDateTime getTimeStamp();
    public abstract String getExceptionName();
    public String getMessage() {
        if (super.getMessage() == null) {
            return "";
        }
        return super.getMessage();
    }

    public Throwable getCause() {
        if (super.getCause() == null) {
            return null;
        }
        return super.getCause();
    }
}
