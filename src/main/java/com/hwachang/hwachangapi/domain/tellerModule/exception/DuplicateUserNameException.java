package com.hwachang.hwachangapi.domain.tellerModule.exception;

import com.hwachang.hwachangapi.utils.exception.CustomExceptionData;
import com.hwachang.hwachangapi.utils.exception.GlobalException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class DuplicateUserNameException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;
    private final String exceptionName = "Duplicate UsernameException";
    public DuplicateUserNameException(String message) {
        super(message);
        customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return LocalDateTime.now();
    }
}
