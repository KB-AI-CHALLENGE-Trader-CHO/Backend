package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BandEvent {
    break_upper,
    break_lower,
    touch_upper,
    touch_lower,
    inside,
    unknown;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case break_upper -> "상단 돌파";
            case break_lower -> "하단 이탈";
            case touch_upper -> "상단 접촉";
            case touch_lower -> "하단 접촉";
            case inside -> "밴드 내부";
            case unknown -> "데이터 부족";
            default -> this.name();
        };
    }

    @JsonCreator
    public static BandEvent fromString(String text) {
        if (text == null) return null;
        for (BandEvent event : BandEvent.values()) {
            if (event.toKorean().equalsIgnoreCase(text) || event.name().equalsIgnoreCase(text)) {
                return event;
            }
        }
        return unknown;
    }
}