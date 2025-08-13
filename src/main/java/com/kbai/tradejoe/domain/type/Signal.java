package com.kbai.tradejoe.domain.type;

public enum Signal {
    POSITIVE("✅"),
    NEGATIVE("🚨"),
    CAUTION("⚠️"),
    UNKNOWN("❔");

    private final String mark;
    Signal(String mark) { this.mark = mark; }
    public String mark() { return mark; }
}