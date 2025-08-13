package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Trend {
    uptrend,
    downtrend,
    sideways;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case uptrend -> "상승추세";
            case downtrend -> "하락추세";
            case sideways -> "횡보";
            default -> this.name();
        };
    }

    @JsonCreator
    public static Trend fromString(String text) {
        if (text == null) return null;

        for (Trend trend : Trend.values()) {
            // 1. 한글 이름과 비교 (대소문자 무시)
            if (trend.toKorean().equalsIgnoreCase(text)) {
                return trend;
            }
            // 2. 영문 이름(기본값)과 비교 (대소문자 무시)
            if (trend.name().equalsIgnoreCase(text)) {
                return trend;
            }
        }
        // 일치하는 값이 없으면 null 또는 예외 처리
        return null;
    }
}