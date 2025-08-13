package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaStack {
    bullish(Signal.POSITIVE, Signal.NEGATIVE),
    bearish(Signal.NEGATIVE, Signal.POSITIVE),
    mixed(  Signal.CAUTION,  Signal.CAUTION);

    private final Signal buy;
    private final Signal sell;

    MaStack(Signal buy, Signal sell) { this.buy = buy; this.sell = sell; }

    public Signal buySignal()  { return buy; }
    public Signal sellSignal() { return sell; }

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case bullish -> "정배열";
            case bearish -> "역배열";
            case mixed   -> "혼조세";
        };
    }

    @JsonCreator
    public static MaStack fromString(String text) {
        if (text == null) return null;
        for (MaStack m : values()) {
            if (m.toKorean().equalsIgnoreCase(text) || m.name().equalsIgnoreCase(text)) return m;
        }
        return null;
    }
}