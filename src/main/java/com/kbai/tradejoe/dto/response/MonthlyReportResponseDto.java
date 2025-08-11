package com.kbai.tradejoe.dto.response;

import com.kbai.tradejoe.domain.MonthlyAnalysis;
import com.kbai.tradejoe.domain.MonthlyReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MonthlyReportResponseDto(
        Long reportId,
        LocalDate period,
        String summary,
        List<MonthlyAnalysisDto> analyses
) {
    public static MonthlyReportResponseDto of(MonthlyReport report, List<MonthlyAnalysis> list) {
        return new MonthlyReportResponseDto(
                report.getId(),
                report.getPeriod(),
                report.getSummary(),
                list.stream().map(MonthlyAnalysisDto::from).toList()
        );
    }

    public record MonthlyAnalysisDto(
            Long id,
            LocalDateTime dateTime,
            String analysisDetails,
            String suggestion,
            Long stockItemId,
            String stockName,
            String symbol
    ) {
        public static MonthlyAnalysisDto from(MonthlyAnalysis e) {
            return new MonthlyAnalysisDto(
                    e.getId(),
                    e.getDateTime(),
                    e.getAnalysisDetails(),
                    e.getSuggestion(),
                    e.getStockItem().getId(),
                    e.getStockItem().getName(),
                    e.getStockItem().getSymbol()
            );
        }
    }
}