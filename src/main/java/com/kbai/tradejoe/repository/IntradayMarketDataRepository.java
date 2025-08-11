package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.IntradayMarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IntradayMarketDataRepository extends JpaRepository<IntradayMarketData, Long> {
    List<IntradayMarketData> findByStockItem_IdAndDateOrderByIdAsc(Long stockItemId, LocalDate date);
    Optional<IntradayMarketData> findByStockItem_IdAndDate(Long stockItemId, LocalDate date);
}
