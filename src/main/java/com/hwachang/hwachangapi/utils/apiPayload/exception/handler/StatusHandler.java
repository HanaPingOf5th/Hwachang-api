package com.hwachang.hwachangapi.utils.apiPayload.exception.handler;

import com.hwachang.hwachangapi.utils.apiPayload.code.BaseErrorCode;
import com.hwachang.hwachangapi.utils.apiPayload.exception.GeneralException;

public class StatusHandler extends GeneralException {
    public StatusHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}