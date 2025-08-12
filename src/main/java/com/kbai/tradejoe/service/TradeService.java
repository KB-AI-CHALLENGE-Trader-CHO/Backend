package com.kbai.tradejoe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbai.tradejoe.domain.StockItem;
import com.kbai.tradejoe.domain.TradeEvaluation;
import com.kbai.tradejoe.domain.TradeHistory;
import com.kbai.tradejoe.domain.WeeklyAnalysis;
import com.kbai.tradejoe.dto.ExceptionDto;
import com.kbai.tradejoe.dto.request.TradeRequestDto;
import com.kbai.tradejoe.dto.response.AnalysisDetailsResponseDto;
import com.kbai.tradejoe.dto.response.TradeResponseDto;
import com.kbai.tradejoe.exception.CommonException;
import com.kbai.tradejoe.exception.ErrorCode;
import com.kbai.tradejoe.repository.StockItemRepository;
import com.kbai.tradejoe.repository.TradeHistoryRepository;
import com.kbai.tradejoe.repository.WeeklyAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final EvaluationService evaluationService;
    private final TradeHistoryRepository tradeHistoryRepository;
    private final StockItemRepository stockItemRepository;
    private final WeeklyAnalysisRepository weeklyAnalysisRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Boolean createTrade(TradeRequestDto requestDto) {
        TradeHistory tradeHistory = requestDto.toEntity();
        StockItem stockItem = stockItemRepository.findById(requestDto.stockItemId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));
        tradeHistory.addStockItem(stockItem);
        tradeHistoryRepository.save(tradeHistory);

//        evaluationService.evaluateAndSave(tradeHistory);
        // --- [수정] 2. EvaluationService 호출하고 결과 받기 ---
        TradeEvaluation evaluation = evaluationService.evaluateAndSave(tradeHistory);

        // --- [추가] 3. WeeklyAnalysis 생성 및 저장 ---
        createAndSaveWeeklyAnalysis(tradeHistory, evaluation);

        return true;
    }

    // [추가] WeeklyAnalysis 생성을 위한 private 헬퍼 메소드
    private void createAndSaveWeeklyAnalysis(TradeHistory tradeHistory, TradeEvaluation evaluation) {
        try {
            // 분석 데이터를 DTO로 변환
            AnalysisDetailsResponseDto detailsDto = AnalysisDetailsResponseDto.from(evaluation);
            // DTO를 JSON 문자열로 변환
            String analysisDetailsJson = objectMapper.writeValueAsString(detailsDto);

            // WeeklyAnalysis 객체 생성
            WeeklyAnalysis newWeeklyAnalysis = WeeklyAnalysis.builder()
                    .tradeHistory(tradeHistory)
                    .analysisDetails(analysisDetailsJson)
                    .suggestion(null) // suggestion은 나중에 채우기 위해 null로 설정
                    .build();

            log.info("newWeeklyAnalysis: {}", newWeeklyAnalysis);
            weeklyAnalysisRepository.save(newWeeklyAnalysis);

        } catch (JsonProcessingException e) {
            // JSON 변환 중 오류 발생 시 예외 처리
            // 실제 프로덕션에서는 로깅 후 더 적절한 예외를 던지는 것이 좋습니다.
            throw new CommonException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public Map<LocalDate, List<TradeResponseDto>> getTradeList() {

        return tradeHistoryRepository.findAll().stream()
                .map(TradeResponseDto::fromEntity)
                .collect(Collectors.groupingBy(TradeResponseDto::date));
    }

    @Transactional
    public Boolean updateTrade(Long id, TradeRequestDto requestDto) {
        TradeHistory tradeHistory = tradeHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with id: " + id));

        tradeHistory.updateTradeDetails(requestDto);

        return true;
    }
}

