package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Trend {
    uptrend(Signal.POSITIVE, Signal.NEGATIVE),
    downtrend(Signal.NEGATIVE, Signal.POSITIVE),
    sideways(Signal.CAUTION,  Signal.CAUTION);

    private final Signal buy;
    private final Signal sell;

    Trend(Signal buy, Signal sell) { this.buy = buy; this.sell = sell; }

    public Signal buySignal()  { return buy; }
    public Signal sellSignal() { return sell; }

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case uptrend -> "상승추세";
            case downtrend -> "하락추세";
            case sideways -> "횡보";
        };
    }

    @JsonCreator
    public static Trend fromString(String text) {
        if (text == null) return null;
        for (Trend t : values()) {
            if (t.toKorean().equalsIgnoreCase(text) || t.name().equalsIgnoreCase(text)) return t;
        }
        return null;
    }
}