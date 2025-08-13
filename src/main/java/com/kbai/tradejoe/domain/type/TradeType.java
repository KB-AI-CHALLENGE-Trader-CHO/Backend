package com.kbai.tradejoe.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TradeType {
    BUY,
    SELL;

    @JsonValue
    public String toKorean() {
        return switch (this) {
            case BUY -> "매수";
            case SELL -> "매도";
            default -> this.name();
        };
    }

    @JsonCreator
    public static TradeType fromString(String text) {
        if (text == null) return null;
        for (TradeType type : TradeType.values()) {
            if (type.toKorean().equalsIgnoreCase(text) || type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
