package com.kbai.tradejoe.repository;

import com.kbai.tradejoe.domain.WeeklyReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    Optional<WeeklyReport> findFirstByPeriodBetween(LocalDate startInclusive, LocalDate endInclusive);

    @Query("""
            SELECT w FROM WeeklyReport w
            WHERE w.period <= :period
            ORDER BY w.period DESC
            """)
    List<WeeklyReport> findByPeriod(LocalDate period, Pageable pageable);
}
