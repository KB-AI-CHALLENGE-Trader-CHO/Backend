package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AtrRegime {
    high(Signal.CAUTION,  Signal.CAUTION),
    mid( Signal.POSITIVE, Signal.POSITIVE),
    low( Signal.POSITIVE, Signal.POSITIVE),
    unknown(Signal.UNKNOWN, Signal.UNKNOWN);

    private final Signal buy;
    private final Signal sell;

    AtrRegime(Signal buy, Signal sell) { this.buy = buy; this.sell = sell; }

    public Signal buySignal()  { return buy; }
    public Signal sellSignal() { return sell; }

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case low     -> "낮은 변동성";
            case mid     -> "평균 변동성";
            case high    -> "높은 변동성";
            case unknown -> "데이터 부족";
        };
    }

    @JsonCreator
    public static AtrRegime fromString(String text) {
        if (text == null) return null;
        for (AtrRegime r : values()) {
            if (r.toKorean().equalsIgnoreCase(text) || r.name().equalsIgnoreCase(text)) return r;
        }
        return unknown;
    }
}