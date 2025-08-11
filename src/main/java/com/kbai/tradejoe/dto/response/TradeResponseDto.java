package com.kbai.tradejoe.dto.response;

import com.kbai.tradejoe.domain.TradeHistory;
import com.kbai.tradejoe.domain.type.TradeType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TradeResponseDto(
        Long id,
        LocalDate date,
        LocalTime time,
        String name,
        String symbol,
        TradeType type,
        Double quantity,
        Double price,
        Double avgBuyPrice,
        String memo
) {
    // TODO: EntityGraph
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