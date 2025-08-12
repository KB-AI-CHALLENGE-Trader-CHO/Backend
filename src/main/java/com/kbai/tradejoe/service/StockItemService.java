package com.kbai.tradejoe.service;

import com.kbai.tradejoe.dto.response.StockItemResponseDto;
import com.kbai.tradejoe.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockItemService {

    private final StockItemRepository stockItemRepository;

    public List<StockItemResponseDto> getStockItemList() {

        return stockItemRepository.findAll().stream()
                .map(StockItemResponseDto::fromEntity)
                .toList();
    }
}
