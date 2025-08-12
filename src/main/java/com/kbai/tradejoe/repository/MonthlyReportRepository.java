package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.MonthlyReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
    Optional<MonthlyReport> findFirstByPeriodBetween(LocalDate startInclusive, LocalDate endInclusive);

    @Query("""
            SELECT w FROM MonthlyReport w
            WHERE w.period <= :period
            ORDER BY w.period DESC
            """)
    List<MonthlyReport> findByPeriod(LocalDate period, Pageable pageable);
}