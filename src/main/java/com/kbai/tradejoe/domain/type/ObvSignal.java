package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ObvSignal {
    bullish(Signal.POSITIVE, Signal.NEGATIVE),
    bearish(Signal.NEGATIVE, Signal.POSITIVE),
    none(   Signal.UNKNOWN,  Signal.UNKNOWN);

    private final Signal buy;
    private final Signal sell;

    ObvSignal(Signal buy, Signal sell) { this.buy = buy; this.sell = sell; }

    public Signal buySignal()  { return buy; }
    public Signal sellSignal() { return sell; }

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case bullish -> "상승 다이버전스";
            case bearish -> "하락 다이버전스";
            case none    -> "신호 없음";
        };
    }

    @JsonCreator
    public static ObvSignal fromString(String text) {
        if (text == null) return null;
        for (ObvSignal s : values()) {
            if (s.toKorean().equalsIgnoreCase(text) || s.name().equalsIgnoreCase(text)) return s;
        }
        return none;
    }
}