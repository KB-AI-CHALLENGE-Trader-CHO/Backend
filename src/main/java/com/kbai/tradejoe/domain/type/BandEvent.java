package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BandEvent {
    break_upper(Signal.POSITIVE, Signal.NEGATIVE),
    break_lower(Signal.NEGATIVE, Signal.POSITIVE),
    touch_upper(Signal.CAUTION,  Signal.CAUTION), // 표에는 'inside'만 있었지만 접촉은 보수적으로 주의 처리
    touch_lower(Signal.CAUTION,  Signal.CAUTION),
    inside(     Signal.CAUTION,  Signal.CAUTION),
    unknown(    Signal.UNKNOWN,  Signal.UNKNOWN);

    private final Signal buy;
    private final Signal sell;

    BandEvent(Signal buy, Signal sell) { this.buy = buy; this.sell = sell; }

    public Signal buySignal()  { return buy; }
    public Signal sellSignal() { return sell; }

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case break_upper -> "상단 돌파";
            case break_lower -> "하단 이탈";
            case touch_upper -> "상단 접촉";
            case touch_lower -> "하단 접촉";
            case inside      -> "밴드 내부";
            case unknown     -> "데이터 부족";
        };
    }

    @JsonCreator
    public static BandEvent fromString(String text) {
        if (text == null) return null;
        for (BandEvent e : values()) {
            if (e.toKorean().equalsIgnoreCase(text) || e.name().equalsIgnoreCase(text)) return e;
        }
        return unknown;
    }
}