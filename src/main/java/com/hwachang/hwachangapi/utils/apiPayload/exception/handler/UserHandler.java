package com.hwachang.hwachangapi.utils.apiPayload.exception.handler;

import com.hwachang.hwachangapi.utils.apiPayload.code.BaseErrorCode;
import com.hwachang.hwachangapi.utils.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}