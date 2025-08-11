package com.kbai.tradejoe.controller;

import com.kbai.tradejoe.dto.ResponseDto;
import com.kbai.tradejoe.dto.request.TradeRequestDto;
import com.kbai.tradejoe.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public ResponseDto<?> createTrade(@RequestBody TradeRequestDto requestDto) {
        return ResponseDto.created(tradeService.createTrade(requestDto));
    }

    @GetMapping
    public ResponseDto<?> getTradeList() {
        return ResponseDto.ok(tradeService.getTradeList());
    }

    @PatchMapping("/{id}")
    public ResponseDto<?> updateTrade(
            @PathVariable Long id,
            @RequestBody TradeRequestDto requestDto
    ) {
        return ResponseDto.ok(tradeService.updateTrade(id, requestDto));
    }
}
