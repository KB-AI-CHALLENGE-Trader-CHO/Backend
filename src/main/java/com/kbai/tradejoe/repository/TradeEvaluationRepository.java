package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.TradeEvaluation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeEvaluationRepository extends JpaRepository<TradeEvaluation, Long> {

    @EntityGraph(attributePaths = {"tradeHistory"})
    Optional<TradeEvaluation> findByTradeHistoryId(Long tradeHistoryId);

    Optional<TradeEvaluation> findByTradeHistory_Id(Long tradeHistoryId);
}
