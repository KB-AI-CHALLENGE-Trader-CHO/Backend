package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.DailyMarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMarketDataRepository extends JpaRepository<DailyMarketData, Long> {
    Optional<DailyMarketData> findByStockItem_IdAndDate(Long stockItemId, LocalDate date);
    List<DailyMarketData> findByStockItem_IdAndDateLessThanEqualOrderByDateAsc(Long stockItemId, LocalDate date);
}