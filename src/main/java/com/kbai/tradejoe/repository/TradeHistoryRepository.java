package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
}
