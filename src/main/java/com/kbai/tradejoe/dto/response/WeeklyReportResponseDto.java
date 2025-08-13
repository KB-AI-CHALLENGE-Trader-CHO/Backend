// DTO: 주간 리포트 + 분석 목록
package com.kbai.tradejoe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kbai.tradejoe.domain.WeeklyAnalysis;
import com.kbai.tradejoe.domain.WeeklyReport;
import com.kbai.tradejoe.domain.type.TradeType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record WeeklyReportResponseDto(
        Long reportId,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate period,
        String summary,
        List<WeeklyAnalysisDto> analyses
) {
    public static WeeklyReportResponseDto of(WeeklyReport report, List<WeeklyAnalysisDto> analyses) {
        return new WeeklyReportResponseDto(
                report.getId(),
                report.getPeriod(),
                report.getSummary(),
                analyses
        );
    }

    // 개별 거래 분석 DTO
    public record WeeklyAnalysisDto(
            Long id,
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate date,
            @JsonFormat(pattern = "HH:mm")
            LocalTime time,
            AnalysisDetailsResponseDto analysisDetails,
            String suggestion,
            String memo,
            String stockName,
            TradeType tradeType
    ) {
        public static WeeklyAnalysisDto from(WeeklyAnalysis e, AnalysisDetailsResponseDto detailsDto) {
            return new WeeklyAnalysisDto(
                    e.getId(),
                    e.getTradeHistory().getDate(),
                    e.getTradeHistory().getTime(),
                    detailsDto,
                    e.getSuggestion(),
                    e.getTradeHistory().getMemo(),
                    e.getTradeHistory().getStockItem().getName(),
                    e.getTradeHistory().getTradeType()
            );
        }
    }
}
