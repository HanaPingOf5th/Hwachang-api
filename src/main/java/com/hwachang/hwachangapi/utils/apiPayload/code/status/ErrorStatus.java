package com.hwachang.hwachangapi.utils.apiPayload.code.status;

import com.hwachang.hwachangapi.utils.apiPayload.code.BaseErrorCode;
import com.hwachang.hwachangapi.utils.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    TELLER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "존재하지 않는 행원입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "존재하지 않는 고객입니다."),

    // 상태 관련 에러
    STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, "STATUS5001", "존재하지 않는 상태입니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "STATUS5002", "유효하지 않은 상태입니다."),

    // 타입 관련 에러
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "TYPE6001", "유효하지 않은 타입입니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}