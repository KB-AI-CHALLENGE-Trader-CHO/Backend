package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

    @Override
    public Optional<StockItem> findById(Long id);
}
