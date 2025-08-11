package com.kbai.tradejoe.dto.request;

import com.kbai.tradejoe.domain.TradeHistory;
import com.kbai.tradejoe.domain.type.TradeType;
import com.kbai.tradejoe.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


public record TradeRequestDto(
        LocalDate date,
        LocalTime time,
        TradeType type,
        Double quantity,
        Double price,
        Double avgBuyPrice,
        String memo,
        Long stockItemId
) {
    public TradeHistory toEntity() {
        return TradeHistory.builder()
                .date(date)
                .time(time)
                .tradeType(type)
                .quantity(quantity)
                .price(price)
                .avgBuyPrice(avgBuyPrice)
                .memo(memo)
                .build();
    }
}