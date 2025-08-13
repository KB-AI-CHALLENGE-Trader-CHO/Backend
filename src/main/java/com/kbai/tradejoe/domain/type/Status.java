package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    normal,
    overbought,
    oversold,
    unknown;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case normal -> "정상";
            case overbought -> "과매수";
            case oversold -> "과매도";
            case unknown -> "데이터 부족";
            default -> this.name();
        };
    }

    @JsonCreator
    public static Status fromString(String text) {
        if (text == null) return null;
        for (Status status : Status.values()) {
            if (status.toKorean().equalsIgnoreCase(text) || status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return unknown; // 기본값으로 UNKNOWN 반환
    }
}