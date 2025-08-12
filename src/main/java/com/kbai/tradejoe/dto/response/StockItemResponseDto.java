package com.kbai.tradejoe.dto.response;

import com.kbai.tradejoe.domain.StockItem;

public record StockItemResponseDto(
        Long id,
        String name,
        String symbol
) {
    public static StockItemResponseDto fromEntity(StockItem entity) {
        return new StockItemResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getSymbol()
        );
    }
}
