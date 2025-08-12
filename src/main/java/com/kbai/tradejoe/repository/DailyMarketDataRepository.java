package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.DailyMarketData;
import com.kbai.tradejoe.domain.StockItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DailyMarketDataRepository extends JpaRepository<DailyMarketData, Long> {

    @Query("""
           SELECT d FROM DailyMarketData d
           WHERE d.stockItem = :stockItem
             AND d.date <= :endDate
           ORDER BY d.date DESC
           """)
    List<DailyMarketData> findRecent100(@Param("stockItem") StockItem stockItem,
                                        @Param("endDate") LocalDate endDate,
                                        Pageable pageable);
}