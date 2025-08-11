package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.TradeHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {

    @Override
    @EntityGraph(attributePaths = {"stockItem"})
    List<TradeHistory> findAll();
}
