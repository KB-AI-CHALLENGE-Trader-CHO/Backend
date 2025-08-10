package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
}
