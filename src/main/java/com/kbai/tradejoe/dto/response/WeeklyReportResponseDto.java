// DTO: 주간 리포트 + 분석 목록
package com.kbai.tradejoe.dto.response;

import com.kbai.tradejoe.domain.WeeklyAnalysis;
import com.kbai.tradejoe.domain.WeeklyReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record WeeklyReportResponseDto(
        Long reportId,
        LocalDate period,
        String summary,
        List<WeeklyAnalysisDto> analyses
) {
    public static WeeklyReportResponseDto of(WeeklyReport report, List<WeeklyAnalysis> list) {
        return new WeeklyReportResponseDto(
                report.getId(),
                report.getPeriod(),
                report.getSummary(),
                list.stream().map(WeeklyAnalysisDto::from).toList()
        );
    }

    public record WeeklyAnalysisDto(
            Long id,
            LocalDateTime dateTime,
            String analysisDetails,
            String suggestion,
            Long tradeHistoryId
    ) {
        public static WeeklyAnalysisDto from(WeeklyAnalysis e) {
            return new WeeklyAnalysisDto(
                    e.getId(),
                    e.getDateTime(),
                    e.getAnalysisDetails(),
                    e.getSuggestion(),
                    e.getTradeHistory().getId()
            );
        }
    }
}
