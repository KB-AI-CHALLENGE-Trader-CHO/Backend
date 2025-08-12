package com.kbai.tradejoe.controller;

import com.kbai.tradejoe.dto.ResponseDto;
import com.kbai.tradejoe.service.StockItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-items")
@RequiredArgsConstructor
public class StockItemController {

    private final StockItemService stockItemService;

    @GetMapping
    public ResponseDto<?> getStockItemList() {
        return ResponseDto.ok(stockItemService.getStockItemList());
    }

}
