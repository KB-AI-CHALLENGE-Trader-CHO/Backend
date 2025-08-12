package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.IntradayMarketData;
import com.kbai.tradejoe.domain.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IntradayMarketDataRepository extends JpaRepository<IntradayMarketData, Long> {

    @Query("""
           SELECT i FROM IntradayMarketData i
           WHERE i.stockItem = :stockItem
             AND i.date >= :from
             AND i.date <= :to
           ORDER BY i.date ASC
           """)
    List<IntradayMarketData> findInRange(@Param("stockItem") StockItem stockItem,
                                         @Param("from") LocalDateTime from,
                                         @Param("to") LocalDateTime to);
}
