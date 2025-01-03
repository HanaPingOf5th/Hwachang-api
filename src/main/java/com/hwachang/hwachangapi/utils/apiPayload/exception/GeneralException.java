package com.hwachang.hwachangapi.utils.apiPayload.exception;

import com.hwachang.hwachangapi.utils.apiPayload.code.BaseErrorCode;
import com.hwachang.hwachangapi.utils.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}