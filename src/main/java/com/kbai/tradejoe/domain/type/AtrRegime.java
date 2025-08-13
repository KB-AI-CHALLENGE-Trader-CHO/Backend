package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AtrRegime {
    low,
    mid,
    high,
    unknown;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case low -> "낮은 변동성";
            case mid -> "평균 변동성";
            case high -> "높은 변동성";
            case unknown -> "데이터 부족";
            default -> this.name();
        };
    }

    @JsonCreator
    public static AtrRegime fromString(String text) {
        if (text == null) return null;
        for (AtrRegime regime : AtrRegime.values()) {
            if (regime.toKorean().equalsIgnoreCase(text) || regime.name().equalsIgnoreCase(text)) {
                return regime;
            }
        }
        return unknown;
    }
}