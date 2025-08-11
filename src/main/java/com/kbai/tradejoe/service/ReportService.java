// Service 구현
package com.kbai.tradejoe.service;

import com.kbai.tradejoe.domain.MonthlyAnalysis;
import com.kbai.tradejoe.domain.MonthlyReport;
import com.kbai.tradejoe.domain.WeeklyAnalysis;
import com.kbai.tradejoe.domain.WeeklyReport;
import com.kbai.tradejoe.dto.response.MonthlyReportResponseDto;
import com.kbai.tradejoe.dto.response.WeeklyReportResponseDto;
import com.kbai.tradejoe.repository.MonthlyAnalysisRepository;
import com.kbai.tradejoe.repository.MonthlyReportRepository;
import com.kbai.tradejoe.repository.WeeklyAnalysisRepository;
import com.kbai.tradejoe.repository.WeeklyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final WeeklyAnalysisRepository weeklyAnalysisRepository;
    private final WeeklyReportRepository weeklyReportRepository;
    private final MonthlyAnalysisRepository monthlyAnalysisRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    public WeeklyReportResponseDto getWeeklyReport() {
        // 서버 시간대: Asia/Seoul 기준
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // 한국(월요일 시작) 기준 주의 시작/끝 계산
        WeekFields wf = WeekFields.of(Locale.KOREA);
        LocalDate weekStart = today.with(wf.dayOfWeek(), 1); // 월
        LocalDate weekEnd   = today.with(wf.dayOfWeek(), 7); // 일

        WeeklyReport report = weeklyReportRepository
                .findFirstByPeriodBetween(weekStart, weekEnd)
                .orElseThrow(() -> new IllegalStateException("이번 주 WeeklyReport가 존재하지 않습니다."));

        List<WeeklyAnalysis> analyseList = weeklyAnalysisRepository.findAllByWeeklyReportId(report.getId());

        return WeeklyReportResponseDto.of(report, analyseList);
    }

    public MonthlyReportResponseDto getMonthlyReport() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd   = today.withDayOfMonth(today.lengthOfMonth());

        MonthlyReport report = monthlyReportRepository
                .findFirstByPeriodBetween(monthStart, monthEnd)
                .orElseThrow(() -> new IllegalStateException("이번 달 MonthlyReport가 존재하지 않습니다."));

        List<MonthlyAnalysis> analyseList = monthlyAnalysisRepository.findAllByMonthlyReportId(report.getId());

        return MonthlyReportResponseDto.of(report, analyseList);
    }
}
