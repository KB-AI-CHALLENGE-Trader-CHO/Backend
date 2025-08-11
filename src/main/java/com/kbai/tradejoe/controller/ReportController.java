package com.kbai.tradejoe.controller;

import com.kbai.tradejoe.dto.ResponseDto;
import com.kbai.tradejoe.dto.request.TradeRequestDto;
import com.kbai.tradejoe.service.ReportService;
import com.kbai.tradejoe.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/weekly")
    public ResponseDto<?> getWeeklyReport() {
        return ResponseDto.ok(reportService.getWeeklyReport());
    }

    @GetMapping("/monthly")
    public ResponseDto<?> getMonthlyReport() {
        return ResponseDto.ok(reportService.getMonthlyReport());
    }
}
