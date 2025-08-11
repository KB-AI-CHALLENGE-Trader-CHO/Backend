package com.kbai.tradejoe.service;

import com.kbai.tradejoe.domain.StockItem;
import com.kbai.tradejoe.domain.TradeHistory;
import com.kbai.tradejoe.dto.request.TradeRequestDto;
import com.kbai.tradejoe.dto.response.TradeResponseDto;
import com.kbai.tradejoe.exception.CommonException;
import com.kbai.tradejoe.exception.ErrorCode;
import com.kbai.tradejoe.repository.StockItemRepository;
import com.kbai.tradejoe.repository.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeHistoryRepository tradeHistoryRepository;
    private final StockItemRepository stockItemRepository;

    @Transactional
    public Boolean createTrade(TradeRequestDto requestDto) {
        TradeHistory tradeHistory = requestDto.toEntity();
        StockItem stockItem = stockItemRepository.findById(requestDto.stockItemId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));
        tradeHistory.addStockItem(stockItem);
        tradeHistoryRepository.save(tradeHistory);

        return true;
    }

    public Map<LocalDate, List<TradeResponseDto>> getTradeList() {
        List<TradeResponseDto> tradeHistoryList = tradeHistoryRepository.findAll().stream().map(TradeResponseDto::fromEntity).toList();

        return tradeHistoryRepository.findAll().stream()
                .map(TradeResponseDto::fromEntity)
                .collect(Collectors.groupingBy(TradeResponseDto::date));
    }

    @Transactional
    public Boolean updateTrade(Long id, TradeRequestDto requestDto) {
        TradeHistory tradeHistory = tradeHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with id: " + id));

        tradeHistory.updateTradeDetails(requestDto);

        return true;
    }
}

