package com.kbai.tradejoe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbai.tradejoe.domain.MonthlyAnalysis;
import com.kbai.tradejoe.domain.MonthlyReport;
import com.kbai.tradejoe.domain.WeeklyAnalysis;
import com.kbai.tradejoe.domain.WeeklyReport;
import com.kbai.tradejoe.dto.response.AnalysisDetailsResponseDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final WeeklyAnalysisRepository weeklyAnalysisRepository;
    private final WeeklyReportRepository weeklyReportRepository;
    private final MonthlyAnalysisRepository monthlyAnalysisRepository;
    private final MonthlyReportRepository monthlyReportRepository;

    // [추가] ObjectMapper 의존성 주입
    private final ObjectMapper objectMapper;

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

        // [추가] 분석 목록을 최종 DTO 리스트로 변환하는 로직
        List<WeeklyReportResponseDto.WeeklyAnalysisDto> analysisDtos = analyseList.stream()
                .map(this::convertWeeklyAnalysisToDto)
                .toList();

        return WeeklyReportResponseDto.of(report, analysisDtos);
    }
    public MonthlyReportResponseDto getMonthlyReportById(Long id) {
        MonthlyReport report = monthlyReportRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));
        List<MonthlyAnalysis> analyseList = monthlyAnalysisRepository.findAllByMonthlyReportId(report.getId());

        return MonthlyReportResponseDto.of(report, analyseList);
    }

    public WeeklyReportResponseDto getDefaultWeeklyReport() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        List<WeeklyReport> report = weeklyReportRepository.findByPeriod(today, Pageable.ofSize(1));

        if (report == null || report.isEmpty()) return null;

        List<WeeklyAnalysis> analyseList = weeklyAnalysisRepository.findAllByWeeklyReportId(report.getLast().getId());

        // [추가] 분석 목록을 최종 DTO 리스트로 변환하는 로직
        List<WeeklyReportResponseDto.WeeklyAnalysisDto> analysisDtos = analyseList.stream()
                .map(this::convertWeeklyAnalysisToDto)
                .toList();

        return WeeklyReportResponseDto.of(report.getLast(), analysisDtos);
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


    // TODO: AOP 형식으로 수정, Exception 처리
    private WeeklyReportResponseDto.WeeklyAnalysisDto convertWeeklyAnalysisToDto(WeeklyAnalysis analysis) {
        try {
            // DB에서 가져온 JSON 문자열을 AnalysisDetailsDto 객체로 파싱
            AnalysisDetailsResponseDto detailsDto = objectMapper.readValue(analysis.getAnalysisDetails(), AnalysisDetailsResponseDto.class);
            // 수정된 from 메소드를 사용하여 최종 DTO 생성
            return WeeklyReportResponseDto.WeeklyAnalysisDto.from(analysis, detailsDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse analysis_details for weekly_analysis_id: {}", analysis.getId(), e);
            // 파싱 실패 시, details는 null로 보내는 등 예외 처리
            return WeeklyReportResponseDto.WeeklyAnalysisDto.from(analysis, null);
        }
    }
}
