package com.hwachang.hwachangapi.domain.tellerModule.entities;

public enum Type {
    // 개인 금융
    SAVINGS("예금"),
    FUND_TRUST("펀드/신탁"),
    CARD("카드"),
    INSURANCE("보험"),
    LOAN("대출"),
    SMART_BANKING("스마트뱅킹"),
    CERTIFICATE("인증서"),
    HOUSING_SUBSCRIPTION("주택 청약"),
    TELE_BANKING("텔레뱅킹"),
    FINANCIAL_FRAUD("금융사기"),
    AUTOMATIC_TRANSFER("자동이체"),
    OTHER_PERSONAL("기타"),

    // 기업 금융
    CORPORATE_LOAN("대출"),
    FOREIGN_EXCHANGE("외환"),
    NOTIFICATION("입출금 알림"),
    OPEN_BANKING("오픈 뱅킹"),
    CORPORATE_SAVINGS("예금"),
    CORPORATE_AUTOMATIC_TRANSFER("자동이체"),
    CORPORATE_FINANCIAL_FRAUD("금융 사기"),
    CORPORATE_TELE_BANKING("텔레뱅킹"),
    CORPORATE_CERTIFICATE("인증서"),
    VIP("우수고객"),
    OTHER_CORPORATE("기타");

    private final String description;

    Type(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
