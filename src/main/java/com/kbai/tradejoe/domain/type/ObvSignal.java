package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ObvSignal {
    none,
    bullish,
    bearish;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case bullish -> "상승 다이버전스";
            case bearish -> "하락 다이버전스";
            case none -> "신호 없음";
            default -> this.name();
        };
    }

    @JsonCreator
    public static ObvSignal fromString(String text) {
        if (text == null) return null;
        for (ObvSignal signal : ObvSignal.values()) {
            if (signal.toKorean().equalsIgnoreCase(text) || signal.name().equalsIgnoreCase(text)) {
                return signal;
            }
        }
        return none;
    }
}