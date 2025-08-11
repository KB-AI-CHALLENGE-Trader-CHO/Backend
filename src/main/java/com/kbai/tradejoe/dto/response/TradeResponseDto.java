package com.kbai.tradejoe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kbai.tradejoe.domain.TradeHistory;
import com.kbai.tradejoe.domain.type.TradeType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TradeResponseDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        String name,
        String symbol,
        TradeType type,
        Double quantity,
        Double price,
        Double avgBuyPrice,
        String memo
) {
    public static TradeResponseDto fromEntity(TradeHistory entity) {
        return new TradeResponseDto(
                entity.getId(),
                entity.getDate(),
                entity.getTime(),
                entity.getStockItem().getName(),
                entity.getStockItem().getSymbol(),
                entity.getTradeType(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getAvgBuyPrice(),
                entity.getMemo()
        );
    }
}