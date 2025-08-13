package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    overbought(Signal.NEGATIVE, Signal.POSITIVE),
    oversold(  Signal.POSITIVE, Signal.NEGATIVE),
    normal(    Signal.POSITIVE, Signal.POSITIVE),
    unknown(   Signal.UNKNOWN,  Signal.UNKNOWN);

    private final Signal buy;
    private final Signal sell;

    Status(Signal buy, Signal sell) { this.buy = buy; this.sell = sell; }

    public Signal buySignal()  { return buy; }
    public Signal sellSignal() { return sell; }

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case normal     -> "정상";
            case overbought -> "과매수";
            case oversold   -> "과매도";
            case unknown    -> "데이터 부족";
        };
    }

    @JsonCreator
    public static Status fromString(String text) {
        if (text == null) return null;
        for (Status s : values()) {
            if (s.toKorean().equalsIgnoreCase(text) || s.name().equalsIgnoreCase(text)) return s;
        }
        return unknown;
    }
}