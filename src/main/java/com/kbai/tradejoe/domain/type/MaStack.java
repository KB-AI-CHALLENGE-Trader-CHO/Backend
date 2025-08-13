package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaStack {
    bullish,
    bearish,
    mixed;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case bullish -> "정배열";
            case bearish -> "역배열";
            case mixed -> "혼조세";
            default -> this.name();
        };
    }

    @JsonCreator
    public static MaStack fromString(String text) {
        if (text == null) return null;
        for (MaStack stack : MaStack.values()) {
            if (stack.toKorean().equalsIgnoreCase(text) || stack.name().equalsIgnoreCase(text)) {
                return stack;
            }
        }
        return null;
    }
}