package com.kbai.tradejoe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kbai.tradejoe.domain.MonthlyAnalysis;
import com.kbai.tradejoe.domain.MonthlyReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MonthlyReportResponseDto(
        Long reportId,
        @JsonFormat(pattern = "yyyy-MM-dd")
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
            String analysisDetails,
            String suggestion,
            String stockName
    ) {
        public static MonthlyAnalysisDto from(MonthlyAnalysis e) {
            return new MonthlyAnalysisDto(
                    e.getId(),
                    e.getAnalysisDetails(),
                    e.getSuggestion(),
                    e.getStockItem().getName()
            );
        }
    }
}