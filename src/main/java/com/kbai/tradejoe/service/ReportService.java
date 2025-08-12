package com.kbai.tradejoe.service;

import com.kbai.tradejoe.domain.MonthlyAnalysis;
import com.kbai.tradejoe.domain.MonthlyReport;
import com.kbai.tradejoe.domain.WeeklyAnalysis;
import com.kbai.tradejoe.domain.WeeklyReport;
import com.kbai.tradejoe.dto.response.MonthlyReportResponseDto;
import com.kbai.tradejoe.dto.response.ReportOrderDto;
import com.kbai.tradejoe.dto.response.WeeklyReportResponseDto;
import com.kbai.tradejoe.exception.CommonException;
import com.kbai.tradejoe.exception.ErrorCode;
import com.kbai.tradejoe.repository.MonthlyAnalysisRepository;
import com.kbai.tradejoe.repository.MonthlyReportRepository;
import com.kbai.tradejoe.repository.WeeklyAnalysisRepository;
import com.kbai.tradejoe.repository.WeeklyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final WeeklyAnalysisRepository weeklyAnalysisRepository;
    private final WeeklyReportRepository weeklyReportRepository;
    private final MonthlyAnalysisRepository monthlyAnalysisRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    public Map<Integer, List<ReportOrderDto>> getWeeklyReportList() {
        WeekFields wf = WeekFields.of(java.util.Locale.KOREA);

        return weeklyReportRepository.findAll().stream()
                .sorted(Comparator.comparing(WeeklyReport::getPeriod))
                .collect(Collectors.groupingBy(
                        r -> r.getPeriod().get(wf.weekBasedYear()),
                        TreeMap::new,
                        Collectors.mapping(
                                r -> new ReportOrderDto(
                                        r.getId(),
                                        r.getPeriod().get(wf.weekOfYear())
                                ),
                                Collectors.toList()
                        )
                ));
    }

    public Map<Integer, List<ReportOrderDto>> getMonthlyReportList() {
        WeekFields wf = WeekFields.of(java.util.Locale.KOREA);

        return monthlyReportRepository.findAll().stream()
                .sorted(Comparator.comparing(MonthlyReport::getPeriod))
                .collect(Collectors.groupingBy(
                        r -> r.getPeriod().get(wf.weekBasedYear()),
                        TreeMap::new,
                        Collectors.mapping(
                                r -> new ReportOrderDto(
                                        r.getId(),
                                        r.getPeriod().getMonthValue()
                                ),
                                Collectors.toList()
                        )
                ));
    }

    public WeeklyReportResponseDto getWeeklyReportById(Long id) {
        WeeklyReport report = weeklyReportRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));
        List<WeeklyAnalysis> analyseList = weeklyAnalysisRepository.findAllByWeeklyReportId(report.getId());

        return WeeklyReportResponseDto.of(report, analyseList);
    }

    public MonthlyReportResponseDto getMonthlyReportById(Long id) {
        MonthlyReport report = monthlyReportRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));
        List<MonthlyAnalysis> analyseList = monthlyAnalysisRepository.findAllByMonthlyReportId(report.getId());

        return MonthlyReportResponseDto.of(report, analyseList);
    }

    public WeeklyReportResponseDto getDefaultWeeklyReport() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        List<WeeklyReport> report = weeklyReportRepository.findByPeriod(today, Pageable.ofSize(1));

        if (report == null || report.isEmpty()) {
            return null;
        }

        List<WeeklyAnalysis> analyseList = weeklyAnalysisRepository.findAllByWeeklyReportId(report.getLast().getId());

        return WeeklyReportResponseDto.of(report.getLast(), analyseList);
    }

    public MonthlyReportResponseDto getDefaultMonthlyReport() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        List<MonthlyReport> report = monthlyReportRepository.findByPeriod(today, Pageable.ofSize(1));

        if (report == null || report.isEmpty()) {
            return null;
        }

        List<MonthlyAnalysis> analyseList = monthlyAnalysisRepository.findAllByMonthlyReportId(report.getLast().getId());

        return MonthlyReportResponseDto.of(report.getLast(), analyseList);
    }
}
