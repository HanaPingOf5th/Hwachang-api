package com.hwachang.hwachangapi.utils.apiPayload.code;

public interface BaseErrorCode {
    public ErrorReasonDTO getReason();
    public ErrorReasonDTO getReasonHttpStatus();
}