package com.hwachang.hwachangapi.utils.apiPayload.exception.handler;

import com.hwachang.hwachangapi.utils.apiPayload.code.BaseErrorCode;
import com.hwachang.hwachangapi.utils.apiPayload.exception.GeneralException;

public class TypeHandler extends GeneralException {
    public TypeHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}