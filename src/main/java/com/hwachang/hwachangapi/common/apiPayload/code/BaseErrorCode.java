package com.hwachang.hwachangapi.common.apiPayload.code;

public interface BaseErrorCode {
    public ErrorReasonDTO getReason();
    public ErrorReasonDTO getReasonHttpStatus();
}