package com.kbai.tradejoe.controller;

import com.kbai.tradejoe.dto.ResponseDto;
import com.kbai.tradejoe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/weekly")
    public ResponseDto<?> getWeeklyReport() {
        return ResponseDto.ok(reportService.getDefaultWeeklyReport());
    }

    @GetMapping("/monthly")
    public ResponseDto<?> getMonthlyReport() {
        return ResponseDto.ok(reportService.getDefaultMonthlyReport());
    }

    @GetMapping("/weekly/{id}")
    public ResponseDto<?> getWeeklyReportById(@PathVariable("id") Long id) {
        return ResponseDto.ok(reportService.getWeeklyReportById(id));
    }

    @GetMapping("/monthly/{id}")
    public ResponseDto<?> getMonthlyReportById(@PathVariable("id") Long id) {
        return ResponseDto.ok(reportService.getMonthlyReportById(id));
    }

    @GetMapping("/weekly/list")
    public ResponseDto<?> getWeeklyReportList() {
        return ResponseDto.ok(reportService.getWeeklyReportList());
    }

    @GetMapping("/monthly/list")
    public ResponseDto<?> getMonthlyReportList() {
        return ResponseDto.ok(reportService.getMonthlyReportList());
    }
}
